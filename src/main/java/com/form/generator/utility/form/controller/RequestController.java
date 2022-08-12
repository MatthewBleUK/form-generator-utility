package com.form.generator.utility.form.controller;

import javax.servlet.http.HttpSession;

import com.form.generator.utility.authentication.login.AuthenticationHelper;
import com.form.generator.utility.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RequestController {

	private final AuthenticationHelper authenticationHelper;

	public RequestController(AuthenticationHelper authenticationHelper) {

		this.authenticationHelper = authenticationHelper;
	}

	@ModelAttribute("isAuthenticated")
	public boolean populateIsAuthenticated(HttpSession session) {

		return session.getAttribute("currentUser") != null;
	}

	@GetMapping("/dashboard")
	public String requestsPage(HttpSession session, Model model) throws Exception {

		User currentUser = authenticationHelper.tryGetUser(session);
		model.addAttribute("currentUser", currentUser);

		return "profile";
	}
}
