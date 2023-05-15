package com.funixproductions.api.service.google.recaptcha.config;

import com.funixproductions.api.service.google.recaptcha.services.GoogleCaptchaService;
import com.funixproductions.core.tools.network.IPUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("google.recaptcha.key")
public class FunixProductionsCaptchaService implements GoogleCaptchaConfig {

    private String site;

    private String secret;

    private Float threshold;

    private boolean disabled;

    @Bean
    public GoogleCaptchaService googleCaptchaService(IPUtils ipUtils) {
        return new GoogleCaptchaService(this, ipUtils);
    }

}
