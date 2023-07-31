package com.funixproductions.api.user.service.services;

import com.funixproductions.api.core.enums.FrontOrigins;
import com.funixproductions.api.google.gmail.client.clients.GoogleGmailClient;
import com.funixproductions.api.google.gmail.client.dto.MailDTO;
import com.funixproductions.api.user.client.dtos.requests.UserPasswordResetDTO;
import com.funixproductions.api.user.client.dtos.requests.UserPasswordResetRequestDTO;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.entities.UserPasswordReset;
import com.funixproductions.api.user.service.repositories.UserPasswordResetRepository;
import com.funixproductions.api.user.service.repositories.UserRepository;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.tools.string.PasswordGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserResetService {

    private final UserRepository userRepository;
    private final UserPasswordResetRepository userPasswordResetRepository;
    private final GoogleGmailClient googleGmailClient;

    @Transactional
    public void resetPasswordRequest(final UserPasswordResetRequestDTO request) {
        final Iterable<User> usersSearch = this.userRepository.findAllByEmail(request.getEmail());

        for (final User user : usersSearch) {
            final MailDTO resetMail = generateResetMail(user, request.getOrigin());
            this.googleGmailClient.sendMail(resetMail, Collections.singletonList(user.getEmail()));
        }
    }

    public void resetPassword(final UserPasswordResetDTO request) {

    }

    private MailDTO generateResetMail(final User user, final FrontOrigins origin) {
        final UserPasswordReset passwordReset = this.generateNewResetToken(user, origin);
        final String mailBody = generateResetMailBody(user, origin, passwordReset.getResetToken());
        final MailDTO mailDTO = new MailDTO();

        mailDTO.setSubject(String.format("Réinitialisation du mot de passe sur %s.", origin.getHumanReadableOrigin()));
        mailDTO.setBodyText(mailBody);
        return mailDTO;
    }

    private String generateResetMailBody(final User user,
                                         final FrontOrigins origin,
                                         final String resetToken) {
        //TODO read reset-mail.html and replace placeholders
    }

    private UserPasswordReset generateNewResetToken(final User user, final FrontOrigins origin) {
        final PasswordGenerator passwordGenerator = new PasswordGenerator();
        passwordGenerator.setAlphaDown(50);
        passwordGenerator.setAlphaUpper(50);
        passwordGenerator.setNumbersAmount(50);
        passwordGenerator.setSpecialCharsAmount(50);

        try {
            final UserPasswordReset passwordReset = new UserPasswordReset(user, passwordGenerator.generateRandomPassword(), origin);
            return this.userPasswordResetRepository.save(passwordReset);
        } catch (Exception e) {
            final String errorMessage = "Erreur interne lors de la génération du token de réinitialisation de mot de passe.";

            log.error(errorMessage, e);
            throw new ApiException(errorMessage, e);
        }
    }

}
