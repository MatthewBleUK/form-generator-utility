package com.form.generator.utility.user.service;

import java.time.Instant;

import com.form.generator.utility.user.User;
import com.form.generator.utility.user.VerificationToken;
import com.form.generator.utility.user.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

	private final UserService userService;

	private final TokenRepository tokenRepository;

	public TokenService(UserService userService, TokenRepository tokenRepository) {

		this.userService = userService;
		this.tokenRepository = tokenRepository;
	}

	public User findUserByToken(String token) throws Exception {

		VerificationToken verificationToken = tokenRepository.getVerificationToken(token).stream().findFirst().get();

		User user = userService.findUser(verificationToken.getUser().getEmail()).get(0);
		validateTokenExpiryDate(verificationToken);

		return user;
	}

	public void validateTokenExpiryDate(VerificationToken verificationToken) throws Exception {

		if (!verificationToken.getExpiryDate().toInstant().isAfter(Instant.now())) {

			LOGGER.error("Token expired");
			throw new Exception("Token expired");
		}
	}
}
