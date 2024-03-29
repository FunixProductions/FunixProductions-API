package com.funixproductions.api.twitch.auth.service.resources;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.auth.service.services.TwitchClientTokenService;
import com.funixproductions.api.twitch.auth.service.services.TwitchServerTokenService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("kubeinternal/twitch/auth")
@RequiredArgsConstructor
public class TwitchInternalAuthResource implements TwitchInternalAuthClient {

    private final TwitchClientTokenService twitchClientTokenService;
    private final TwitchServerTokenService twitchServerTokenService;

    @Override
    public TwitchClientTokenDTO fetchToken(String userUUID) {
        if (Strings.isBlank(userUUID)) {
            throw new ApiBadRequestException("Le userUUID ne doit pas être vide.");
        }

        return twitchClientTokenService.fetchToken(UUID.fromString(userUUID));
    }

    @Override
    public TwitchClientTokenDTO fetchTokenByStreamerName(String streamerName) {
        return twitchClientTokenService.fetchTokenByStreamerUsername(streamerName);
    }

    @Override
    public TwitchClientTokenDTO fetchTokenByStreamerId(String streamerId) {
        return twitchClientTokenService.fetchTokenByStreamerId(streamerId);
    }

    @Override
    public String fetchServerToken() {
        return twitchServerTokenService.getAccessToken();
    }
}
