package com.funixproductions.api.twitch.reference.service.resources.users;

import com.funixproductions.api.client.twitch.auth.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.client.twitch.reference.clients.users.TwitchUsersClient;
import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.user.TwitchFollowDTO;
import com.funixproductions.api.service.twitch.auth.services.TwitchClientTokenService;
import com.funixproductions.api.service.twitch.reference.resources.TwitchReferenceResource;
import com.funixproductions.api.service.twitch.reference.services.users.TwitchReferenceUsersService;
import com.funixproductions.api.service.user.services.CurrentSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/users")
public class TwitchUsersResource extends TwitchReferenceResource implements TwitchUsersClient {

    private final TwitchReferenceUsersService service;

    public TwitchUsersResource(CurrentSession currentSession,
                               TwitchClientTokenService tokenService,
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
