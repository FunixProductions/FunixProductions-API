package com.funixproductions.api.twitch.reference.service.resources.users;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.reference.client.clients.users.TwitchUsersClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchFollowDTO;
import com.funixproductions.api.twitch.reference.service.resources.TwitchReferenceResource;
import com.funixproductions.api.twitch.reference.service.services.users.TwitchReferenceUsersService;
import com.funixproductions.api.user.client.security.CurrentSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/users")
public class TwitchUsersResource extends TwitchReferenceResource implements TwitchUsersClient {

    private final TwitchReferenceUsersService service;

    public TwitchUsersResource(CurrentSession currentSession,
                               TwitchInternalAuthClient tokenService,
                               TwitchReferenceUsersService service) {
        super(tokenService, currentSession);
        this.service = service;
    }

    @Override
    public TwitchDataResponseDTO<TwitchFollowDTO> isUserFollowingStreamer(String userId, String streamerId) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        return service.isUserFollowingStreamer(
                tokenDTO.getAccessToken(),
                userId,
                streamerId
        );
    }
}
