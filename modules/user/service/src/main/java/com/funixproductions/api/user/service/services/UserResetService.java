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
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.tools.network.IPUtils;
import com.funixproductions.core.tools.string.PasswordGenerator;
import com.funixproductions.core.tools.string.StringUtils;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserResetService {

    private static final int COOLDOWN_REQUEST_SPAM = 5;

    private final String resetMailTemplate;
    private final String resetMailDoneTemplate;

    private final UserRepository userRepository;
    private final UserPasswordResetRepository userPasswordResetRepository;
    private final GoogleGmailClient googleGmailClient;
    private final IPUtils ipUtils;

    private final Cache<Long, Integer> triesCache = CacheBuilder.newBuilder().expireAfterWrite(COOLDOWN_REQUEST_SPAM, TimeUnit.MINUTES).build();

    public UserResetService(final UserRepository userRepository,
                            final UserPasswordResetRepository userPasswordResetRepository,
                            final GoogleGmailClient googleGmailClient,
                            final IPUtils ipUtils) {
        this.userRepository = userRepository;
        this.userPasswordResetRepository = userPasswordResetRepository;
        this.googleGmailClient = googleGmailClient;
        this.ipUtils = ipUtils;
        this.resetMailTemplate = StringUtils.readFromClasspath("user/reset-mail.html", this.getClass());
        this.resetMailDoneTemplate = StringUtils.readFromClasspath("user/reset-mail-done.html", this.getClass());
    }

    @Transactional
    public void resetPasswordRequest(final UserPasswordResetRequestDTO request, final HttpServletRequest servletRequest) {
        final Iterable<User> usersSearch = this.userRepository.findAllByEmail(request.getEmail());
        final String ipClient = this.ipUtils.getClientIp(servletRequest);

        try {
            for (final User user : usersSearch) {
                if (this.triesCache.getIfPresent(user.getId()) == null) {
                    final MailDTO resetMail = generateResetMail(user, request.getOrigin(), ipClient);
                    this.googleGmailClient.sendMail(resetMail, new String[]{user.getEmail()});
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
            if (!request.getNewPassword().equals(request.getNewPasswordConfirmation())) {
                throw new ApiBadRequestException("Les mots de passe ne correspondent pas.");
            }

            final String resetToken = new String(Base64.getDecoder().decode(request.getResetToken()), StandardCharsets.UTF_8);
            final UserPasswordReset userPasswordReset = this.userPasswordResetRepository.findByResetToken(resetToken)
                    .orElseThrow(() -> new ApiBadRequestException("Le token de réinitialisation est invalide."));

            final User user = userPasswordReset.getUser();

            user.setPassword(request.getNewPassword());

            this.userRepository.save(user);
            this.userPasswordResetRepository.delete(userPasswordReset);
            this.triesCache.invalidate(user.getId());

            final MailDTO successMail = generateResetDoneMail(user, userPasswordReset.getOrigin(), ipClient);
            this.googleGmailClient.sendMail(successMail, new String[]{user.getEmail()});
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
        final String mailBody = generateResetMailBody(user, origin, null, ipClient);

        mailDTO.setSubject(String.format("Réinitialisation du mot de passe sur %s réussi.", origin.getHumanReadableOrigin()));
        mailDTO.setBodyText(mailBody);
        return mailDTO;
    }

    private MailDTO generateResetMail(final User user, final FrontOrigins origin, final String ipClient) {
        final UserPasswordReset passwordReset = this.generateNewResetToken(user, origin);
        final String mailBody = generateResetMailBody(user, origin, passwordReset.getResetToken(), ipClient);
        final MailDTO mailDTO = new MailDTO();

        mailDTO.setSubject(String.format("Réinitialisation du mot de passe sur %s.", origin.getHumanReadableOrigin()));
        mailDTO.setBodyText(mailBody);
        return mailDTO;
    }

    private String generateResetMailBody(@NonNull final User user,
                                         @NonNull final FrontOrigins origin,
                                         @Nullable final String resetToken,
                                         @NonNull final String ipClient) {
        final String mailBody;

        if (Strings.isNullOrEmpty(resetToken)) {
            mailBody = this.resetMailDoneTemplate;
        } else {
            final String urlRedirect = getUrlRedirect(origin, resetToken);
            mailBody = this.resetMailTemplate.replace("{{URL_PASSWORD_RESET}}", urlRedirect);
        }

        return mailBody.replace("{{USERNAME}}", user.getUsername())
                .replace("{{IP_ADDRESS}}", ipClient)
                .replace("{{ORIGIN_WEBSITE}}", origin.getHumanReadableOrigin());
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
