package com.funixproductions.api.user.service.services;

import com.funixproductions.api.google.gmail.client.clients.GoogleGmailClient;
import com.funixproductions.api.google.gmail.client.dto.MailDTO;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.entities.UserValidAccountToken;
import com.funixproductions.api.user.service.repositories.UserRepository;
import com.funixproductions.api.user.service.repositories.UserValidAccountTokenRepository;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.exceptions.ApiNotFoundException;
import com.funixproductions.core.tools.string.PasswordGenerator;
import com.funixproductions.core.tools.string.StringUtils;
import com.google.common.base.Strings;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserValidationAccountService {

    private final GoogleGmailClient googleGmailClient;
    private final UserValidAccountTokenRepository userValidAccountTokenRepository;
    private final UserRepository userRepository;
    private final String mailTemplate;

    public UserValidationAccountService(GoogleGmailClient googleGmailClient,
                                        UserRepository userRepository,
                                        UserValidAccountTokenRepository userValidAccountTokenRepository) {
        this.googleGmailClient = googleGmailClient;
        this.userRepository = userRepository;
        this.userValidAccountTokenRepository = userValidAccountTokenRepository;
        this.mailTemplate = StringUtils.readFromClasspath("user/valid-account-mail.html", this.getClass());
    }

    @Transactional
    public void sendMailValidationRequest(final User user) {
        final String validToken = generateNewValidToken(user);
        final MailDTO mailDTO = generateMailValidationRequest(user, validToken);

        try {
            this.googleGmailClient.sendMail(mailDTO, new String[]{user.getEmail()});
        } catch (Exception e) {
            log.error("Erreur interne lors de l'envoi du mail de validation pour le compte {}", user.getUsername(), e);
            throw new ApiException("Erreur interne lors de l'envoi du mail de validation pour le compte " + user.getUsername(), e);
        }
    }

    @Transactional
    public void sendMailValidationRequest(final UUID userUuid) {
        final Optional<User> search = this.userRepository.findByUuid(userUuid.toString());

        if (search.isPresent()) {
            sendMailValidationRequest(search.get());
        } else {
            throw new ApiNotFoundException("Le compte id " + userUuid + " n'existe pas.");
        }
    }

    @Transactional
    public void validateAccount(final String token) {
        final Optional<UserValidAccountToken> search = this.userValidAccountTokenRepository.findByValidationToken(token);

        if (search.isPresent()) {
            final UserValidAccountToken userValidAccountToken = search.get();
            final User user = userValidAccountToken.getUser();

            user.setValid(true);
            this.userRepository.save(user);
            this.userValidAccountTokenRepository.delete(userValidAccountToken);
        } else {
            throw new ApiBadRequestException("Token invalide. Veuillez en demander un nouveau.");
        }
    }

    private String generateNewValidToken(final User user) {
        if (Boolean.TRUE.equals(user.getValid())) {
            throw new ApiBadRequestException("Le compte est déjà validé.");
        }

        try {
            final Optional<UserValidAccountToken> oldToken = this.userValidAccountTokenRepository.findByUser(user);
            oldToken.ifPresent(this.userValidAccountTokenRepository::delete);

            final PasswordGenerator passwordGenerator = new PasswordGenerator();
            UserValidAccountToken userValidAccountToken = new UserValidAccountToken();

            passwordGenerator.setNumbersAmount(40);
            passwordGenerator.setAlphaUpper(40);
            passwordGenerator.setAlphaDown(40);
            passwordGenerator.setSpecialCharsAmount(40);
            userValidAccountToken.setUser(user);
            final String token = Base64.getEncoder().encodeToString(passwordGenerator.generateRandomPassword().getBytes());
            userValidAccountToken.setValidationToken(token);

            userValidAccountToken = userValidAccountTokenRepository.save(userValidAccountToken);
            return userValidAccountToken.getValidationToken();
        } catch (Exception e) {
            log.error("Erreur interne lors de la génération du token de validation de compte pour le compte {}", user.getUsername(), e);
            throw new ApiException("Erreur interne lors de la génération du token de validation de compte pour le compte " + user.getUsername(), e);
        }
    }

    private MailDTO generateMailValidationRequest(final User user, final String validToken) {
        final String mailContent = mailTemplate
                .replace("{{URL_VALIDATION}}", generateUrlValidation(validToken))
                .replace("{{USERNAME}}", user.getUsername());
        final MailDTO mailDTO = new MailDTO();

        mailDTO.setBodyText(mailContent);
        mailDTO.setSubject("Validation de votre compte.");
        return mailDTO;
    }

    private String generateUrlValidation(final String validToken) {
        final String env = System.getenv("SPRING_PROFILES_ACTIVE");
        final boolean isDev = !Strings.isNullOrEmpty(env) && env.contains("dev");
        final String domain;

        if (isDev) {
            domain = "https://dev.api.funixproductions.com";
        } else {
            domain = "https://api.funixproductions.com";
        }
        return domain + "/user/auth/valid-account?token=" + validToken;
    }

}
