package com.form.generator.utility.authentication.confirmation;

import javax.validation.Valid;

import com.form.generator.utility.authentication.registration.RegistrationController;
import com.form.generator.utility.user.User;
import com.form.generator.utility.user.service.TokenService;
import com.form.generator.utility.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@SuppressWarnings("unused")
public class UserConfirmationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

	private final UserService userService;
	private final TokenService tokenService;

	public UserConfirmationController(UserService userService, TokenService tokenService) {

		this.userService = userService;
		this.tokenService = tokenService;
	}

	@RequestMapping(value = "/confirmation/{token}", method = RequestMethod.GET)
	public String verifyUser(
			@PathVariable() String token,
			@Valid @ModelAttribute("user") User user,
			BindingResult bindingResult,
			Model model) {

		try {

			if (bindingResult.hasErrors()) {

				return "login";
			}

			User tokenUser = tokenService.findUserByToken(token);
			userService.enableUserAccount(tokenUser);

			return "redirect:/login";

		} catch (Exception exception) {

			LOGGER.error(exception.getMessage());
			model.addAttribute("message", exception.getMessage());
		}

		return "/login";
	}
}
