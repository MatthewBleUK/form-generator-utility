package com.form.generator.utility.authentication.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import com.form.generator.utility.authentication.registration.validation.EmailValidation;
import com.form.generator.utility.authentication.registration.validation.UserValidation;
import com.form.generator.utility.authentication.registration.validation.FieldValidation;
import com.form.generator.utility.authentication.registration.validation.PasswordValidation;
import com.form.generator.utility.user.dto.UserDto;
import com.form.generator.utility.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Validation {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailValidation.class);

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

        String email = userDto.getEmail();

        // the concurrent latch is used, so we can validate all the fields at the same time,
        // while not allowing the program to continue until this process is finished
        CountDownLatch countDownLatch = new CountDownLatch(4);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        try {

            List<Runnable> runnables = new ArrayList<>();
            List<Future<?>> futures = new ArrayList<>();

            runnables.add(new FieldValidation(userDto, countDownLatch));
            runnables.add(new EmailValidation(email, countDownLatch));
            runnables.add(new UserValidation(email, userService, countDownLatch));
            runnables.add(new PasswordValidation(userDto.getPassword(), userDto.getMatchingPassword(), countDownLatch));

            for (Runnable runnable : runnables) {

                futures.add(executor.submit(runnable));
            }

            for (Future<?> future : futures) {

                future.get();
            }

            countDownLatch.await();

        } catch (Exception ex) {

            LOGGER.error("Error while validating registration", ex);
            throw new Exception(ex.getMessage().replace("java.lang.RuntimeException: ", ""));

        } finally {

            shutdownAndAwaitTermination(executor);
        }
    }

    private static void shutdownAndAwaitTermination(ExecutorService executor) {

        executor.shutdown();

        try {

            if (!executor.awaitTermination(30, TimeUnit.MINUTES)) {

                executor.shutdownNow();
            }

        } catch (InterruptedException e) {

            executor.shutdownNow();
        }
    }
}