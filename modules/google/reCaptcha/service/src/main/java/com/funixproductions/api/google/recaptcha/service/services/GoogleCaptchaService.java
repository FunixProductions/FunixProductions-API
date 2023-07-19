package com.funixproductions.api.google.recaptcha.service.services;

import com.funixproductions.api.google.recaptcha.client.services.GoogleRecaptchaHandler;
import com.funixproductions.api.google.recaptcha.service.clients.GoogleRecaptchaClient;
import com.funixproductions.api.google.recaptcha.service.config.GoogleCaptchaConfig;
import com.funixproductions.api.google.recaptcha.service.dtos.GoogleCaptchaSiteVerifyResponseDTO;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiForbiddenException;
import com.funixproductions.core.tools.network.IPUtils;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class GoogleCaptchaService {
    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
    private static final int MAX_ATTEMPT = 8;

    private final GoogleCaptchaConfig googleCaptchaConfig;
    private final GoogleRecaptchaClient googleRecaptchaClient;
    private final IPUtils ipUtils;

    private final Cache<String, Integer> triesCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).build();
    private final Gson gson = new Gson();

    public void checkCode(final @NonNull HttpServletRequest request,
                          final @NonNull String actionCode) {
        if (Strings.isNullOrEmpty(actionCode) || actionCode.isBlank()) {
            throw new ApiBadRequestException("Action code empty.");
        }

        final String captchaCode = request.getHeader(GoogleRecaptchaHandler.GOOGLE_CODE_HEADER);
        if (captchaCode == null) {
            throw new ApiBadRequestException("Le code google reCaptcha header est invalide. (code null)");
        }
        final String clientIp = ipUtils.getClientIp(request);

        if (isBlocked(clientIp)) {
            log.info("Client ip {} is blocked due to too many requests.", clientIp);
            throw new ApiForbiddenException(String.format("Vous avez fait plus de %d essais. Vous êtes donc bloqué" +
                    " 15 minutes. Veuillez réessayer plus tard.", MAX_ATTEMPT));
        }

        if (StringUtils.hasLength(captchaCode) && RESPONSE_PATTERN.matcher(captchaCode).matches()) {
            try {
                makeCallToGoogle(captchaCode, clientIp);
            } catch (ApiBadRequestException e) {
                log.info("Header {} value: {}", IPUtils.HEADER_X_FORWARDED, request.getHeader(IPUtils.HEADER_X_FORWARDED));
                throw e;
            }
        } else {
            throw new ApiBadRequestException("Le code google reCaptcha est invalide. (match invalide)");
        }
    }

    private void makeCallToGoogle(@NonNull final String captchaCode, @NonNull final String clientIp) {
        try {
            final GoogleCaptchaSiteVerifyResponseDTO response = this.googleRecaptchaClient.verify(
                    this.googleCaptchaConfig.getSecret(),
                    captchaCode,
                    clientIp,
                    ""
            );

            if (response.isValidCaptcha(captchaCode, this.googleCaptchaConfig.getThreshold())) {
                reCaptchaSucceeded(clientIp);
            } else {
                reCaptchaFailed(clientIp);
                log.info("Client ip {} failed to verify reCaptcha.", clientIp);
                throw new ApiBadRequestException("Le code google reCaptcha est invalide. (google refus)");
            }
        } catch (FeignException e) {
            log.info("Client ip {} failed to verify reCaptcha. Error", clientIp, e);
            throw new ApiBadRequestException("Une erreur est survenue lors de la vérification du captcha auprès de google. (status code: " + e.status() + ")");
        }
    }

    private void reCaptchaSucceeded(String key) {
        triesCache.invalidate(key);
    }

    private void reCaptchaFailed(String key) {
        Integer attempts = triesCache.getIfPresent(key);

        if (attempts == null) {
            attempts = 0;
        }
        triesCache.put(key, attempts + 1);
    }

    private boolean isBlocked(@NonNull final String ip) {
        final Integer attempts = triesCache.getIfPresent(ip);
        return attempts != null && attempts >= MAX_ATTEMPT;
    }

}
