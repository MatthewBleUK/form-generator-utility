package com.form.generator.utility.authentication.registration.validation;

import com.form.generator.utility.exceptions.UserValidationException;
import com.form.generator.utility.user.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class FieldValidation implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldValidation.class);

    private final UserDto userDto;

    private final CountDownLatch countDownLatch;

    public FieldValidation(UserDto userDto, CountDownLatch countDownLatch) {

        this.userDto = userDto;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {

        try {

            if (!userDto.getFirstName().isBlank() || !userDto.getLastName().isBlank() || !userDto.getEmail().isBlank()) {

                LOGGER.info("All fields are populated");

            } else {

                LOGGER.error("Empty fields");
                throw new Exception("Fields should not be empty");
            }

        } catch (Exception e) {

            throw new RuntimeException("User Already Registered");

        } finally {

            countDownLatch.countDown();
        }
    }
}
