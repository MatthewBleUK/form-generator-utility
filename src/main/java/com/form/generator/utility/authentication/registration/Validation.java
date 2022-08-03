package com.form.generator.utility.authentication.registration;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.form.generator.utility.user.dto.UserDto;
import com.form.generator.utility.user.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class Validation {

	private final UserService userService;

	public Validation(UserService userService) {

		this.userService = userService;
	}

	/**
	 * Validates if:
	 * <ul>
	 *     <li>User is already registered</li>
	 *     <li>The password and the confirm password match</li>
	 *     <li>A strong password is used: (1 symbol, 1 lowercase and 1 uppercase letter, between 8 and 20 chars</li>
	 * </ul>
	 */
	void validateUser(UserDto userDto) throws Exception {

		checkMissingFields(userDto);
		checkValidEmail(userDto.getEmail());
		isUserAlreadyRegistered(userDto.getEmail());
		validateMatchingPassword(userDto.getPassword(), userDto.getMatchingPassword());
		validateSafePassword(userDto.getPassword());
	}

	private void checkMissingFields(UserDto userDto) throws Exception {

		if (userDto.getFirstName().isBlank() || userDto.getLastName().isBlank() || userDto.getEmail().isBlank()){

			throw new Exception("Fields should not be empty!");
		}
	}

	private void checkValidEmail(String email) throws Exception {

		if(!email.contains("@")) {

			throw new Exception("Enter a valid email!");
		}
	}

	private void isUserAlreadyRegistered(String email) throws Exception {

		if (userService.findUser(email).size() != 0) {

			throw new Exception("User already registered!");
		}
	}

	/**
	 * Validates if the password and confirmation password are the same
	 *
	 * @throws Exception when passwords don't match
	 */
	private void validateMatchingPassword(String password, String matchingPassword) throws Exception {

		if (!Objects.equals(password, matchingPassword)) {

			throw new Exception("Passwords don't match!");
		}
	}

	/**
	 * Method for validating the strength of a password
	 *
	 * @param password should contain
	 * @throws Exception when password isn't between 8 and 20 symbols and doesn't contain at least:
	 *                   <ul>
	 *                    <li> 1 special symbol </li>
	 *                    <li> 1 uppercase letter </li>
	 *                    <li> 1 lowercase letter </li>
	 *                   </ul>
	 */
	private void validateSafePassword(String password) throws Exception {

		if (!isValid(password)) {

			throw new Exception("Password needs to contain" +
					" 1 special symbol, 1 Capital Letter, 1 Lowercase letter and should be between 8 and 20 symbols");
		}
	}

	private boolean isValid(final String password) {

		// digit + lowercase char + uppercase char + symbol
		String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}
}
