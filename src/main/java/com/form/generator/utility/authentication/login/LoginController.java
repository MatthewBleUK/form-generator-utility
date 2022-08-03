package com.form.generator.utility.authentication.login;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.form.generator.utility.user.LoginDTO;
import com.form.generator.utility.user.User;
import com.form.generator.utility.user.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/")
@SuppressWarnings("unused")
public class LoginController {

	private final AuthenticationHelper authenticationHelper;

	public LoginController(AuthenticationHelper authenticationHelper) {

		this.authenticationHelper = authenticationHelper;
	}

	@GetMapping("/login")
	public String showLoginPage(Model model) {

		model.addAttribute("user", new LoginDTO());

		return "login";
	}

	@PostMapping("/login")
	public String handleLogin(
			@Valid @ModelAttribute("user") LoginDTO userDto,
			HttpSession session,
			BindingResult bindingResult,
			Model model) {

		try {

			if (bindingResult.hasErrors()) {

				System.out.println("here");
				return "login";
			}

			authenticationHelper.verifyAuthentication(userDto.getEmail(), userDto.getPassword());
			session.setAttribute("currentUser", userDto.getEmail());

			User user = authenticationHelper.tryGetUser(session);

			return "/index.html";

		} catch (Exception ex) {

			model.addAttribute("message", ex.getMessage());
		}

		return "/login";
	}
}
