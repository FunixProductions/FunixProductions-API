package com.funixproductions.api.google.recaptcha.service.config;

import com.funixproductions.api.google.recaptcha.service.dtos.GoogleCaptchaSiteVerifyResponseDTO;
import com.funixproductions.api.google.recaptcha.service.services.GoogleCaptchaService;
import com.funixproductions.core.tools.network.IPUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
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

    @Bean
    public GoogleCaptchaService googleCaptchaService(IPUtils ipUtils) {
        return new GoogleCaptchaService(this, ipUtils);
    }

}
