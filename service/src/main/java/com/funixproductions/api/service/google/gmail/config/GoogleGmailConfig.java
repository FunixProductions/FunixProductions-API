package com.funixproductions.api.service.google.gmail.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("google.clients.gmail")
public class GoogleGmailConfig {

    public static final String FILE_CREDENTIALS = "gmail-credentials.json";

    /**
     * App client id
     */
    private String clientId;



}
