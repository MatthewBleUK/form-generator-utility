package com.form.generator.utility.controller;

import javax.servlet.http.HttpSession;

import com.form.generator.utility.controller.login.AuthenticationHelper;
import com.form.generator.utility.user.User;
import com.form.generator.utility.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	private final AuthenticationHelper authenticationHelper;
	private final UserService userService;

	public ProfileController(AuthenticationHelper authenticationHelper, UserService userService) {

		this.authenticationHelper = authenticationHelper;
		this.userService = userService;
	}

	@ModelAttribute("user")
	public User populateUser(HttpSession session) throws Exception {

		User user;
		user = authenticationHelper.tryGetUser(session);

		return userService.findUser(user.getEmail()).get(0);
	}

	@GetMapping()
	public String showRegistrationForm() {

		return "/index.html";
	}
}
