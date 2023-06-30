package com.funixproductions.api.twitch.reference.service.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("twitch.api.reference")
public class TwitchReferenceConfig {

    /**
     * Client id from twitch application
     */
    private String appClientId;

    /**
     * Secret from twitch application
     */
    private String appClientSecret;

}
