package com.form.generator.utility.controller.login;

import javax.servlet.http.HttpSession;

import com.form.generator.utility.user.User;
import com.form.generator.utility.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthenticationHelper {

	public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
	public static final String AUTHENTICATION_FAILURE_MESSAGE = "Wrong username or password.";

	private final UserService userService;
	private final PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public AuthenticationHelper(UserService userService, PasswordEncoder bCryptPasswordEncoder) {

		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public User tryGetUser(HttpHeaders headers) {

		if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The requested resource requires authentication.");
		}

		String email = headers.getFirst(AUTHORIZATION_HEADER_NAME);

		return userService.findUser(email).get(0);
	}

	public User tryGetUser(HttpSession session) throws Exception {

		String currentUser = (String) session.getAttribute("currentUser");

		if (currentUser == null) {

			throw new Exception("No user logged in.");
		}

		return userService.findUser(currentUser).get(0);
	}

	public User verifyAuthorization(HttpSession session, String role) throws Exception {

		User user = tryGetUser(session);

/*		Set<String> userRoles =
				user.getRoles().stream().map(r -> r.getType().toLowerCase()).collect(Collectors.toSet());

		if (!userRoles.contains(role.toLowerCase())) {

			throw new Exception("Users does not have the required authorization.");
		}*/

		return user;
	}

	public User verifyAuthentication(String email, String password) throws Exception {

		try {
			
			User user = userService.findUser(email).get(0);

			if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {

				throw new Exception(AUTHENTICATION_FAILURE_MESSAGE);
			}

			return user;

		} catch (Exception e) {

			throw new Exception(AUTHENTICATION_FAILURE_MESSAGE);
		}
	}
}


