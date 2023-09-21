package com.funixproductions.api.twitch.reference.service.services.users;

import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchUserDTO;
import com.funixproductions.api.twitch.reference.client.services.TwitchReferenceService;
import com.funixproductions.api.twitch.reference.service.clients.users.TwitchReferenceUsersClient;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TwitchReferenceUsersService extends TwitchReferenceService implements TwitchReferenceUsersClient {

    private final TwitchReferenceUsersClient client;

    @Override
    public TwitchDataResponseDTO<TwitchUserDTO> getUsersByName(@NonNull final String twitchAccessToken,
                                                               @NonNull final List<String> name) {
        if (name.isEmpty()) {
            throw new ApiBadRequestException("La liste des usernames est vide.");
        }

        try {
            return client.getUsersByName(
                    super.addBearerPrefix(twitchAccessToken),
                    name
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchUserDTO> getUsersById(@NonNull final String twitchAccessToken,
                                                             @NonNull final List<String> id) {
        if (id.isEmpty()) {
            throw new ApiBadRequestException("La liste des id utilisateurs est vide");
        }

        try {
            return client.getUsersById(
                    super.addBearerPrefix(twitchAccessToken),
                    id
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }

}
