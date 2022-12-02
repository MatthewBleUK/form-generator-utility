package com.form.generator.utility.user.service;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.*;

import com.form.generator.utility.notifications.EmailConfirmationThread;
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

import javax.validation.constraints.Email;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

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

	public void create(UserDto userDto) throws InterruptedException {

		User user = new User();

		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setEmail(userDto.getEmail());

		UserDescription userDescription = new UserDescription();

		userDescription.setUser(user);
		userDescription.setFirstName(userDto.getFirstName());
		userDescription.setLastName(userDto.getLastName());

		userRepository.create(user);
		userDescriptionRepository.create(userDescription);

		EmailConfirmationThread emailConfirmationThread =
				new EmailConfirmationThread(gmailOperations, userDto, user, tokenRepository);

		ExecutorService executorService = Executors.newFixedThreadPool(1);

		try {

			executorService.submit(emailConfirmationThread);

		} catch (Exception exception) {

			exception.printStackTrace();

		} finally {

			executorService.shutdown();
		}
	}

	public List<User> findUser(String email) {

		return userRepository.getUserByEmail(email);
	}

	public void enableUserAccount(User user) {

		if (Objects.equals(user.getEnabled(), "n")) {

			user.setEnabled("y");
			userRepository.update(user);

			LOGGER.info("User {} successfully verified email", user.getEmail());
		}
	}
}