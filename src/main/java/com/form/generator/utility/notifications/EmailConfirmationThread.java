package com.form.generator.utility.notifications;

import com.form.generator.utility.user.User;
import com.form.generator.utility.user.VerificationToken;
import com.form.generator.utility.user.dto.UserDto;
import com.form.generator.utility.user.repository.TokenRepository;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Random;

public class EmailConfirmationThread implements Runnable {

    private final GmailOperations gmailOperations;
    private final UserDto userDto;

    private final User user;

    private final TokenRepository tokenRepository;

    public EmailConfirmationThread(
            GmailOperations gmailOperations,
            UserDto userDto,
            User user,
            TokenRepository tokenRepository) {

        this.gmailOperations = gmailOperations;
        this.userDto = userDto;
        this.user = user;
        this.tokenRepository = tokenRepository;
    }

    // TODO ADD CUSTOM EXCEPTIONS
    @Override
    public void run() {

        try {
            gmailOperations.sendEmailForConfirmRegistration(userDto.getEmail(), createVerificationToken(user));

        } catch (IOException | GeneralSecurityException | MessagingException e) {
            throw new RuntimeException(e);
        }
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
}
