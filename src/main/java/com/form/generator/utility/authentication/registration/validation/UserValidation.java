package com.form.generator.utility.authentication.registration.validation;

import com.form.generator.utility.exceptions.UserValidationException;
import com.form.generator.utility.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;

import java.util.concurrent.CountDownLatch;

public class UserValidation implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserValidation.class);

    private final String email;
    private final UserService userService;

    private final CountDownLatch countDownLatch;

    public UserValidation(String email, UserService userService, CountDownLatch countDownLatch) {

        this.email = email;
        this.userService = userService;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {

        try {

            if (userService.findUser(email).size() == 0) {

                LOGGER.info("User email {} not used", email);

            } else {

                LOGGER.error("User with email {} already registered", email);
                throw new Exception("User already registered!");
            }
        } catch (Exception e) {

            throw new RuntimeException("User Already Registered");

        } finally {

            countDownLatch.countDown();
        }
    }
}
