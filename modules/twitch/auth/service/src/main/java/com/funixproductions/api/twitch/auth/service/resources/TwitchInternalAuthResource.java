package com.funixproductions.api.twitch.auth.service.resources;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.auth.client.enums.TwitchClientTokenType;
import com.funixproductions.api.twitch.auth.service.services.TwitchClientTokenService;
import com.funixproductions.api.twitch.auth.service.services.TwitchServerTokenService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import lombok.NonNull;
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
    public TwitchClientTokenDTO fetchToken(String userUUID, @NonNull String tokenType) {
        if (Strings.isBlank(userUUID)) {
            throw new ApiBadRequestException("Le userUUID ne doit pas être vide.");
        }

        return twitchClientTokenService.fetchToken(
                UUID.fromString(userUUID),
                TwitchClientTokenType.getTokenTypeByString(tokenType)
        );
    }

    @Override
    public TwitchClientTokenDTO fetchTokenByStreamerName(String streamerName, @NonNull String tokenType) {
        return twitchClientTokenService.fetchTokenByStreamerUsername(
                streamerName,
                TwitchClientTokenType.getTokenTypeByString(tokenType)
        );
    }

    @Override
    public TwitchClientTokenDTO fetchTokenByStreamerId(String streamerId, @NonNull String tokenType) {
        return twitchClientTokenService.fetchTokenByStreamerId(
                streamerId,
                TwitchClientTokenType.getTokenTypeByString(tokenType)
        );
    }

    @Override
    public String fetchServerToken() {
        return twitchServerTokenService.getAccessToken();
    }
}
