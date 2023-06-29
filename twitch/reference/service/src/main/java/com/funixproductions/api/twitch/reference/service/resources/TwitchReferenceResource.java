package com.funixproductions.api.twitch.reference.service.resources;

import com.funixproductions.api.client.twitch.auth.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.client.user.dtos.UserDTO;
import com.funixproductions.api.service.twitch.auth.services.TwitchClientTokenService;
import com.funixproductions.api.service.user.services.CurrentSession;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TwitchReferenceResource {

    private final TwitchClientTokenService twitchClientTokenService;
    private final CurrentSession currentSession;

    protected TwitchClientTokenDTO getTwitchAuthByUserConnected() throws ApiException {
        final UserDTO userDTO = currentSession.getCurrentUser();
        if (userDTO == null) {
            throw new ApiBadRequestException("Vous n'êtes pas connecté.");
        }

        return twitchClientTokenService.fetchToken(userDTO.getId());
    }

}
