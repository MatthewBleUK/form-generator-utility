package com.form.generator.utility.authentication.registration;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.form.generator.utility.user.User;
import com.form.generator.utility.user.dto.UserDto;
import com.form.generator.utility.user.service.TokenService;
import com.form.generator.utility.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/")
@SuppressWarnings("unused")
public class RegistrationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

	private final Validation validation;
	private final UserService userService;
	private final TokenService tokenService;

	public RegistrationController(UserService userService, Validation validation, TokenService tokenService) {

		this.userService = userService;
		this.validation = validation;
		this.tokenService = tokenService;
	}

	@GetMapping("/sign-up")
	public String showRegistrationForm(WebRequest request, Model model) {

		UserDto userDto = new UserDto();
		model.addAttribute("user", userDto);

		return "sign-up";
	}

	@PostMapping("/sign-up")
	public String registerUser(
			@Valid @ModelAttribute("user") UserDto userDto,
			HttpSession session,
			BindingResult bindingResult,
			Model model) {

		try {

			if (bindingResult.hasErrors()) {

				return "sign-up";
			}

			validation.validateUser(userDto);
			userService.create(userDto);

			// if no errors have been thrown user can now log in
			LOGGER.info("User {} successfully registered", userDto.getEmail());

			model.addAttribute("message", "Please follow the link in your email to activate account");
			return "/login";

		} catch (Exception ex) {

			LOGGER.error(ex.getMessage());
			model.addAttribute("error", ex.getMessage());
		}

		// if any errors have been thrown go back to the same page and display them
		return "/sign-up";
	}

	@RequestMapping(value = "/confirmation/{token}", method = RequestMethod.GET)
	public String verifyUser(
			@PathVariable() String token,
			@Valid @ModelAttribute("user") User user,
			HttpSession session,
			BindingResult bindingResult,
			Model model) {

		try {

			if (bindingResult.hasErrors()) {

				return "login";
			}

			User tokenUser = tokenService.findUserByToken(token);
			userService.enableUserAccount(tokenUser);

			LOGGER.info("User {} successfully verified email", tokenUser.getEmail());
			return "redirect:/confirmation";

		} catch (Exception exception) {

			LOGGER.error(exception.getMessage());
			model.addAttribute("message", exception.getMessage());
		}

		return "/login";
	}

	@GetMapping("/confirmation")
	public String confirmEmail() {

		return "/confirmation";
	}
}
