package com.form.generator.utility.controller.login;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.form.generator.utility.user.LoginDTO;
import com.form.generator.utility.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
@SuppressWarnings("unused")
public class LoginController {

	private final AuthenticationHelper authenticationHelper;

	public LoginController(AuthenticationHelper authenticationHelper) {

		this.authenticationHelper = authenticationHelper;
	}


	@GetMapping("/login")
	public String showRegistrationForm(Model model) {

		model.addAttribute("user", new LoginDTO());

		return "/login";
	}

	@PostMapping("/login")
	public String handleLogin(
			@Valid @ModelAttribute("user") LoginDTO login,
			BindingResult bindingResult,
			HttpSession session,
			Model model) {

		try {
			
			if (bindingResult.hasErrors()) {

				System.out.println("ERROR");
				return "/login";
			}

			authenticationHelper.verifyAuthentication(login.getEmail(), login.getPassword());
			session.setAttribute("currentUser", login.getEmail());

			User user = authenticationHelper.tryGetUser(session);

			return "redirect:/profile";

		} catch (Exception ex) {

			model.addAttribute("message", ex.getMessage());
		}

		return "/login";
	}
}
