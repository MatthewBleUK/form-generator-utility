package com.form.generator.utility.form.controller;

import javax.validation.Valid;

import com.form.generator.utility.form.Message;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-json")
@CrossOrigin("http://localhost:3000")
public class TestJsonResponse {

	@PostMapping
	public void getRequest(
			@Valid @RequestParam String email,
			@Valid @RequestParam String subject,
			@Valid @RequestParam String message) {

		Message messageObject = new Message(email, subject, message);

		System.out.println(messageObject);
	}
}
