package com.form.generator.utility.controller;

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
public class Homepage {
	
	private final AuthenticationHelper authenticationHelper;

	public Homepage(AuthenticationHelper authenticationHelper) {

		this.authenticationHelper = authenticationHelper;
	}

	@ModelAttribute("isAuthenticated")
	public boolean populateIsAuthenticated(HttpSession session) {

		return session.getAttribute("currentUser") != null;
	}

	@GetMapping("/")
	public String showHomePage(Model model, HttpSession session) {

		try {

			User currentUser = authenticationHelper.tryGetUser(session);
			model.addAttribute("currentUser", currentUser);

		} catch (Exception e) {

			model.addAttribute("currentUser", null);
		}

		return "index";
	}
}
