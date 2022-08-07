package com.form.generator.utility.user.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.form.generator.utility.authentication.login.AuthenticationHelper;
import com.form.generator.utility.user.User;
import com.form.generator.utility.user.UserDescription;
import com.form.generator.utility.user.service.UserDescriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class UserController {

	private final AuthenticationHelper authenticationHelper;
	private final UserDescriptionService userDescriptionService;

	public UserController(AuthenticationHelper authenticationHelper, UserDescriptionService userDescriptionService) {

		this.authenticationHelper = authenticationHelper;
		this.userDescriptionService = userDescriptionService;
	}

	@ModelAttribute("isAuthenticated")
	public boolean populateIsAuthenticated(HttpSession session) {

		return session.getAttribute("currentUser") != null;
	}

	@GetMapping("/dashboard")
	public String userProfile(HttpSession session, Model model) throws Exception {

		User currentUser = authenticationHelper.tryGetUser(session);
		model.addAttribute("currentUser", currentUser);

		return "profile";
	}

	@GetMapping("/settings")
	public String userSettings(Model model, HttpSession session) throws Exception {

		User user = authenticationHelper.tryGetUser(session);

		UserDescription userDescription = userDescriptionService.findUserDescription(user.getEmail()).get(0);

		model.addAttribute("name", userDescription.getFirstName() + " " + userDescription.getLastName());
		model.addAttribute("email", user.getEmail());

		return "settings";
	}
}
