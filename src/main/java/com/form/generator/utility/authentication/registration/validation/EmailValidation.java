package com.form.generator.utility.authentication.registration.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class EmailValidation implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailValidation.class);

    private final String email;

    private final CountDownLatch countDownLatch;

    public EmailValidation(String email, CountDownLatch countDownLatch) {

        this.email = email;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {

        try {

            if (email.contains("@")) {

                LOGGER.info("Email {} correct", email);

            } else {

                LOGGER.error("Email {} is incorrect", email);
                throw new Exception("Email is incorrect");
            }
        } catch (Exception e) {

            throw new RuntimeException("Incorrect Email address");

        } finally {

            countDownLatch.countDown();
        }
    }
}
