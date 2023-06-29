package com.funixproductions.api.twitch.auth.service.services;

import com.funixproductions.api.twitch.auth.service.clients.TwitchTokenAuthClient;
import com.funixproductions.api.twitch.auth.service.configurations.TwitchAuthConfig;
import com.funixproductions.api.twitch.auth.service.dtos.TwitchTokenResponseDTO;
import feign.FeignException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter
@Service
@Slf4j(topic = "TwitchAppServerTokenService")
public class TwitchServerTokenService {

    private final TwitchTokenAuthClient twitchTokenAuthClient;
    private final Map<String, String> bodyRequest = new HashMap<>();

    private String accessToken;
    private Instant expiresAt;

    public TwitchServerTokenService(TwitchAuthConfig twitchAuthConfig,
                                    TwitchTokenAuthClient twitchTokenAuthClient) {
        this.twitchTokenAuthClient = twitchTokenAuthClient;

        this.bodyRequest.put("client_id", twitchAuthConfig.getAppClientId());
        this.bodyRequest.put("client_secret", twitchAuthConfig.getAppClientSecret());
        this.bodyRequest.put("grant_type", "client_credentials");

        this.refreshToken();
    }

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void refreshToken() {
        try {
            if (!tokenValid()) {
                final TwitchTokenResponseDTO tokenDTO = twitchTokenAuthClient.getToken(bodyRequest);
                if (tokenDTO == null) {
                    return;
                }

                this.accessToken = tokenDTO.getAccessToken();
                this.expiresAt = Instant.now().plusSeconds(tokenDTO.getExpiresIn());
                log.info("Un nouveau token est généré.");
            }
        } catch (FeignException e) {
            log.error("Une erreur est survenue lors du refresh token du twitch server token.", e);
        }
    }

    private boolean tokenValid() {
        final Instant now = Instant.now();

        if (accessToken == null || expiresAt == null) {
            return false;
        } else {
            return expiresAt.minusSeconds(60).isAfter(now);
        }
    }

}
