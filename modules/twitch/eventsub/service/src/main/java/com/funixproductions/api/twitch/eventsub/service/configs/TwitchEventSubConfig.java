package com.funixproductions.api.twitch.eventsub.service.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("twitch.eventsub")
public class TwitchEventSubConfig {

    /**
     * Key used to sign the payload
     */
    private String hmacSecretKey;

    /**
     * Url callback for the app to add before (domain)/twitch/eventsub/cb
     */
    private String domainUrlAppCallback = "https://api.funixproductions.com";

}
