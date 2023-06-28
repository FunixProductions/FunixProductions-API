package com.funixproductions.api.google.recaptcha.service.config;

import com.funixproductions.api.google.recaptcha.service.dtos.GoogleCaptchaSiteVerifyResponseDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("google.recaptcha")
public class GoogleCaptchaConfig {

    private String site;

    private String secret;

    private Float threshold = GoogleCaptchaSiteVerifyResponseDTO.MINIMAL_THRESHOLD;

    private boolean disabled = false;

}
