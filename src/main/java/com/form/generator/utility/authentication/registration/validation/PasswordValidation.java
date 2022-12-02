package com.form.generator.utility.authentication.registration.validation;

import com.form.generator.utility.exceptions.UserValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidation implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserValidation.class);

    private final String password;
    private final String matchingPassword;
    private final CountDownLatch countDownLatch;

    public PasswordValidation(String password, String matchingPassword, CountDownLatch countDownLatch) {

        this.password = password;
        this.matchingPassword = matchingPassword;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {

        try {

            validateMatchingPassword(password, matchingPassword);
            validateSafePassword(password);

            LOGGER.info("Password successfully validated");

        } catch (Exception ex) {

            LOGGER.error("Error encountered when validating password", ex);
            throw new RuntimeException(ex.getMessage());

        } finally {

            countDownLatch.countDown();
        }
    }

    /**
     * Validates if the password and confirmation password are the same
     *
     * @throws UserValidationException when passwords don't match
     */
    private void validateMatchingPassword(String password, String matchingPassword) throws UserValidationException {

        if (!Objects.equals(password, matchingPassword)) {

            throw new UserValidationException("Passwords don't match!");
        }
    }

    /**
     * Method for validating the strength of a password
     *
     * @param password should contain
     * @throws UserValidationException when password isn't between 8 and 20 symbols and doesn't contain at least:
     *                                 <ul>
     *                                  <li> 1 special symbol </li>
     *                                  <li> 1 uppercase letter </li>
     *                                  <li> 1 lowercase letter </li>
     *                                 </ul>
     */
    private void validateSafePassword(String password) throws UserValidationException {

        if (!isValid(password)) {

            throw new UserValidationException("Password needs to contain" +
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
