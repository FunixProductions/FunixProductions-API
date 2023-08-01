package com.funixproductions.api.user.service.services;

import com.funixproductions.api.core.enums.FrontOrigins;
import com.funixproductions.api.google.gmail.client.clients.GoogleGmailClient;
import com.funixproductions.api.google.gmail.client.dto.MailDTO;
import com.funixproductions.api.user.client.dtos.requests.UserPasswordResetDTO;
import com.funixproductions.api.user.client.dtos.requests.UserPasswordResetRequestDTO;
import com.funixproductions.api.user.service.components.UserPasswordUtils;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.entities.UserPasswordReset;
import com.funixproductions.api.user.service.repositories.UserPasswordResetRepository;
import com.funixproductions.api.user.service.repositories.UserRepository;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.tools.network.IPUtils;
import com.funixproductions.core.tools.string.PasswordGenerator;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserResetService {

    private static final int COOLDOWN_REQUEST_SPAM = 5;

    private static final String FILEPATH_RESET_MAIL = "user/reset-mail.html";
    private static final String FILEPATH_RESET_MAIL_DONE = "user/reset-mail-done.html";

    private final UserRepository userRepository;
    private final UserPasswordUtils userPasswordUtils;
    private final UserPasswordResetRepository userPasswordResetRepository;
    private final GoogleGmailClient googleGmailClient;
    private final IPUtils ipUtils;

    private final Cache<Long, Integer> triesCache = CacheBuilder.newBuilder().expireAfterWrite(COOLDOWN_REQUEST_SPAM, TimeUnit.MINUTES).build();

    @Transactional
    public void resetPasswordRequest(final UserPasswordResetRequestDTO request, final HttpServletRequest servletRequest) {
        final Iterable<User> usersSearch = this.userRepository.findAllByEmail(request.getEmail());
        final String ipClient = this.ipUtils.getClientIp(servletRequest);

        try {
            for (final User user : usersSearch) {
                if (this.triesCache.getIfPresent(user.getId()) == null) {
                    final MailDTO resetMail = generateResetMail(user, request.getOrigin(), ipClient);
                    this.googleGmailClient.sendMail(resetMail, Collections.singletonList(user.getEmail()));
                    this.triesCache.put(user.getId(), 0);
                }
            }
        } catch (FeignException e) {
            final String errorMessage = "Erreur interne lors de l'envoi du mail de réinitialisation.";

            log.error(errorMessage, e);
            throw new ApiException(errorMessage, e);
        }
    }

    @Transactional
    public void resetPassword(final UserPasswordResetDTO request, final HttpServletRequest servletRequest) {
        final String ipClient = this.ipUtils.getClientIp(servletRequest);

        try {
            userPasswordUtils.checkPassword(request.getNewPassword());
            if (!request.getNewPassword().equals(request.getNewPasswordConfirmation())) {
                throw new ApiBadRequestException("Les mots de passe ne correspondent pas.");
            }

            final String resetToken = new String(Base64.getDecoder().decode(request.getResetToken()));
            final UserPasswordReset userPasswordReset = this.userPasswordResetRepository.findByResetToken(resetToken)
                    .orElseThrow(() -> new ApiBadRequestException("Le token de réinitialisation est invalide."));

            final User user = userPasswordReset.getUser();

            user.setPassword(request.getNewPassword());

            this.userRepository.save(user);
            this.userPasswordResetRepository.delete(userPasswordReset);
            this.triesCache.invalidate(user.getId());

            final MailDTO successMail = generateResetDoneMail(user, userPasswordReset.getOrigin(), ipClient);
            this.googleGmailClient.sendMail(successMail, Collections.singletonList(user.getEmail()));
        } catch (ApiException e) {
            throw e;
        } catch (FeignException e) {
            final String errorMessage = "Erreur interne lors de l'envoi du mail de réinitialisation succès.";

            log.error(errorMessage, e);
            throw new ApiException(errorMessage, e);
        } catch (Exception e) {
            final String errorMessage = "Erreur interne lors de la réinitialisation du mot de passe.";

            log.error(errorMessage, e);
            throw new ApiException(errorMessage, e);
        }
    }

    private MailDTO generateResetDoneMail(final User user, final FrontOrigins origin, final String ipClient) {
        final MailDTO mailDTO = new MailDTO();
        final String mailBody = generateResetMailBody(user, origin, "", ipClient, FILEPATH_RESET_MAIL_DONE);

        mailDTO.setSubject(String.format("Réinitialisation du mot de passe sur %s réussi.", origin.getHumanReadableOrigin()));
        mailDTO.setBodyText(mailBody);
        return mailDTO;
    }

    private MailDTO generateResetMail(final User user, final FrontOrigins origin, final String ipClient) {
        final UserPasswordReset passwordReset = this.generateNewResetToken(user, origin);
        final String mailBody = generateResetMailBody(user, origin, passwordReset.getResetToken(), ipClient, FILEPATH_RESET_MAIL);
        final MailDTO mailDTO = new MailDTO();

        mailDTO.setSubject(String.format("Réinitialisation du mot de passe sur %s.", origin.getHumanReadableOrigin()));
        mailDTO.setBodyText(mailBody);
        return mailDTO;
    }

    private String generateResetMailBody(final User user,
                                         final FrontOrigins origin,
                                         final String resetToken,
                                         final String ipClient,
                                         final String filePathMailTemplate) {
        final String urlRedirect = getUrlRedirect(origin, resetToken);

        try (final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filePathMailTemplate)) {
            if (inputStream != null) {
                try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    final StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    return stringBuilder.toString()
                            .replace("{{USERNAME}}", user.getUsername())
                            .replace("{{IP_ADDRESS}}", ipClient)
                            .replace("{{URL_PASSWORD_RESET}}", urlRedirect)
                            .replace("{{ORIGIN_WEBSITE}}", origin.getHumanReadableOrigin());
                }
            } else {
                final String errorMessage = "Erreur interne lors de la génération du corps du mail de réinitialisation de mot de passe. InputStream null read reset-mail.html.";

                log.error(errorMessage);
                throw new ApiException(errorMessage);
            }
        } catch (Exception e) {
            final String errorMessage = "Erreur interne lors de la génération du corps du mail de réinitialisation de mot de passe.";

            log.error(errorMessage, e);
            throw new ApiException(errorMessage, e);
        }
    }

    private String getUrlRedirect(final FrontOrigins origin, final String resetToken) {
        final String env = System.getenv("SPRING_PROFILES_ACTIVE");
        final boolean isDev = !Strings.isNullOrEmpty(env) && env.contains("dev");
        final String urlRedirect;

        if (isDev) {
            urlRedirect = origin.getDomainDev();
        } else {
            urlRedirect = origin.getDomainProd();
        }

        return urlRedirect + "/reset-password?token=" + Base64.getEncoder().encodeToString(resetToken.getBytes());
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

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void checkExpiredPasswordResetTokens() {
        try {
            final Instant instant = Instant.now();
            final Iterable<UserPasswordReset> userPasswordResets = this.userPasswordResetRepository.findAll();
            final Set<UserPasswordReset> expiredTokens = new HashSet<>();

            for (final UserPasswordReset userPasswordReset : userPasswordResets) {
                if (userPasswordReset.getCreatedAt().toInstant().plus(10, ChronoUnit.MINUTES).isBefore(instant)) {
                    expiredTokens.add(userPasswordReset);
                }
            }
            this.userPasswordResetRepository.deleteAll(expiredTokens);
        } catch (Exception e) {
            final String errorMessage = "Erreur interne lors de la vérification des tokens de réinitialisation de mot de passe expirés.";

            log.error(errorMessage, e);
            throw new ApiException(errorMessage, e);
        }
    }

}
