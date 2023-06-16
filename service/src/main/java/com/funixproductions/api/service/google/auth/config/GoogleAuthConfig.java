package com.funixproductions.api.service.google.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("google.clients.auth")
public class GoogleAuthConfig {

    /**
     * Auth client id for public auth
     */
    private String clientId;

}
