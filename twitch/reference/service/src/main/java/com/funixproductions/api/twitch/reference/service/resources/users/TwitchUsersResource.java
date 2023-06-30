package com.funixproductions.api.twitch.reference.service.resources.users;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.reference.client.clients.users.TwitchUsersClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchFollowDTO;
import com.funixproductions.api.twitch.reference.service.services.users.TwitchReferenceUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/reference/users")
@RequiredArgsConstructor
public class TwitchUsersResource implements TwitchUsersClient {

    private final TwitchReferenceUsersService service;
    private final TwitchInternalAuthClient internalAuthClient;

    @Override
    public TwitchDataResponseDTO<TwitchFollowDTO> isUserFollowingStreamer(String userId, String streamerId) {
        return service.isUserFollowingStreamer(
                this.internalAuthClient.fetchServerToken(),
                userId,
                streamerId
        );
    }
}
