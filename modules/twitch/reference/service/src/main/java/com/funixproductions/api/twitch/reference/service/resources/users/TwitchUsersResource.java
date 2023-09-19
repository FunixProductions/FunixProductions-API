package com.funixproductions.api.twitch.reference.service.resources.users;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.reference.client.clients.users.TwitchUsersClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchUserDTO;
import com.funixproductions.api.twitch.reference.service.services.users.TwitchReferenceUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/twitch/reference/users")
@RequiredArgsConstructor
public class TwitchUsersResource implements TwitchUsersClient {

    private final TwitchReferenceUsersService service;
    private final TwitchInternalAuthClient internalAuthClient;

    @Override
    public TwitchDataResponseDTO<TwitchUserDTO> getUsersByName(List<String> name) {
        return service.getUsersByName(
                this.internalAuthClient.fetchServerToken(),
                name
        );
    }

    @Override
    public TwitchDataResponseDTO<TwitchUserDTO> getUsersById(List<String> id) {
        return service.getUsersById(
                this.internalAuthClient.fetchServerToken(),
                id
        );
    }
}
