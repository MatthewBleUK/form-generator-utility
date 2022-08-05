package com.form.generator.utility.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DocumentationController {

	@GetMapping("/tos")
	public String termsOfService() {

		return "tos";
	}

	@GetMapping("/privacy-policy")
	public String privacyPolicy() {

		return "privacy-policy";
	}

	@GetMapping("/support")
	public String support() {

		return "support";
	}
}
