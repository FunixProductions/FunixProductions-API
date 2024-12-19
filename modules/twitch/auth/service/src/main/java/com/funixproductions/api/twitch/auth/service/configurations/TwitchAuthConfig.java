package com.funixproductions.api.twitch.auth.service.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("twitch.api.auth")
public class TwitchAuthConfig {

    /**
     * Domain url callback where the users are redirected before (domain)/twitch/auth/cb
     */
    private String appCallbackDomain = "https://api.funixproductions.com";

    /**
     * To know if the API is in dev mode or not
     */
    private Boolean devMode = false;

}
