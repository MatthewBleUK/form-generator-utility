package com.form.generator.utility.user.service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.form.generator.utility.authentication.registration.RegistrationController;
import com.form.generator.utility.notifications.GmailOperations;
import com.form.generator.utility.user.User;
import com.form.generator.utility.user.UserDescription;
import com.form.generator.utility.user.VerificationToken;
import com.form.generator.utility.user.dto.UserDto;
import com.form.generator.utility.user.repository.TokenRepository;
import com.form.generator.utility.user.repository.UserDescriptionRepository;
import com.form.generator.utility.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

	@Autowired
	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;
	private final UserDescriptionRepository userDescriptionRepository;

	private final TokenRepository tokenRepository;

	private final GmailOperations gmailOperations;

	public UserService(
			PasswordEncoder passwordEncoder,
			UserRepository userRepository,
			UserDescriptionRepository userDescriptionRepository,
			TokenRepository tokenRepository,
			GmailOperations gmailOperations) {

		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.userDescriptionRepository = userDescriptionRepository;
		this.tokenRepository = tokenRepository;
		this.gmailOperations = gmailOperations;
	}

	public void create(UserDto userDto) {

		User user = new User();

		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setEmail(userDto.getEmail());

		UserDescription userDescription = new UserDescription();

		userDescription.setEmail(user);
		userDescription.setFirstName(userDto.getFirstName());
		userDescription.setLastName(userDto.getLastName());

		userRepository.create(user);
		userDescriptionRepository.create(userDescription);

		try {

			gmailOperations.sendEmailForConfirmRegistration(userDto.getEmail(), createVerificationToken(user));

		} catch (Exception exception) {

			exception.printStackTrace();
		}
	}

	public List<User> findUser(String email) {

		return userRepository.getUserByEmail(email);
	}

	public String createVerificationToken(User user) {

		String token = createToken();
		VerificationToken newToken = new VerificationToken(user, token);

		tokenRepository.createToken(newToken);

		return token;
	}

	/**
	 * Creates a random string that will be used as validation token for email
	 */
	private String createToken() {

		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'

		Random random = new Random();

		return random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(16)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
	}

	public void enableUserAccount(User user) {

		if (Objects.equals(user.getEnabled(), "n")) {

			user.setEnabled("y");
			userRepository.update(user);

			LOGGER.info("User {} successfully verified email", user.getEmail());
		}
	}
}