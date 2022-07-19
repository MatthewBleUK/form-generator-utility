package com.form.generator.utility.user.service;

import java.util.List;

import com.form.generator.utility.user.UserDescription;
import com.form.generator.utility.user.repository.UserDescriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserDescriptionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDescriptionService.class);

	private final UserDescriptionRepository userDescriptionRepository;

	public UserDescriptionService(UserDescriptionRepository userDescriptionRepository) {

		this.userDescriptionRepository = userDescriptionRepository;
	}

	public List<UserDescription> findUserDescription(String email) {

		return userDescriptionRepository.getUserDescriptionByEmail(email);
	}
}
