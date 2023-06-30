package com.funixproductions.api.twitch.reference.service.resources;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.security.CurrentSession;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TwitchReferenceResource {

    private final TwitchInternalAuthClient twitchClientTokenService;
    private final CurrentSession currentSession;

    protected TwitchClientTokenDTO getTwitchAuthByUserConnected() throws ApiException {
        final UserDTO userDTO = currentSession.getCurrentUser();
        if (userDTO == null) {
            throw new ApiBadRequestException("Vous n'êtes pas connecté.");
        }

        return twitchClientTokenService.fetchToken(userDTO.getId().toString());
    }

}
