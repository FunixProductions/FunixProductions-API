package com.funixproductions.api.google.recaptcha.client.services;

import com.funixproductions.api.google.recaptcha.client.clients.GoogleRecaptchaInternalClient;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.tools.network.IPUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleRecaptchaHandler {
    public static final String GOOGLE_CODE_HEADER = "X-Captcha-Google-Code";
    public static final String GOOGLE_ACTION_HEADER = "X-Captcha-Google-Action";

    private final GoogleRecaptchaInternalClient googleRecaptchaClient;
    private final IPUtils ipUtils;

    /**
     * Verify the captcha code
     * @param servletRequest spring request
     * @param action action code
     */
    public void verify(final HttpServletRequest servletRequest, final String action) throws ApiBadRequestException {
        final String ip = this.ipUtils.getClientIp(servletRequest);
        final String googleCode = servletRequest.getHeader(GOOGLE_CODE_HEADER);

        try {
            this.googleRecaptchaClient.verify(action, googleCode, ip);
        } catch (FeignException e) {
            log.warn("Google captcha error. Code http {}. Message {}.", e.status(), e.getMessage());
            throw new ApiBadRequestException(String.format("Le captcha est invalide. Code http %s. Message %s.", e.status(), e.getMessage()));
        }
    }

}
