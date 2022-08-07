package com.form.generator.utility.authentication.login;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.form.generator.utility.user.LoginDTO;
import com.form.generator.utility.user.User;
import org.bouncycastle.util.Strings;
import org.jsoup.internal.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
			@Valid @ModelAttribute("user") LoginDTO loginDTO,
			HttpSession session,
			BindingResult bindingResult,
			Model model) {

		try {

			if (bindingResult.hasErrors()) {

				return "login";
			}

			authenticationHelper.verifyAuthentication(loginDTO.getEmail(), loginDTO.getPassword());
			session.setAttribute("currentUser", loginDTO.getEmail());

			// thymeleaf will return true only if the checkbox is checked and null if not checked
			if (!StringUtil.isBlank(loginDTO.getRememberMe()) && loginDTO.getRememberMe().equals("true")) {

				session.setMaxInactiveInterval(30);
			}

			User user = authenticationHelper.tryGetUser(session);

			return "redirect:/profile";

		} catch (Exception ex) {

			model.addAttribute("message", ex.getMessage());
		}

		return "/login";
	}

	@GetMapping("/logout")
	public String handleLogout(HttpSession session) {

		session.removeAttribute("currentUser");

		return "redirect:/";
	}
}
