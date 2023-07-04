package com.funixproductions.api.user.service.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("funixproductions.api.user")
public class UserConfiguration {

    /**
     * Base64 encoded secret key for JWT
     */
    private String jwtSecret;

}
