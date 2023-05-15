package com.funixproductions.api.service.twitch.reference.services.game;

import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.game.TwitchGameDTO;
import com.funixproductions.api.service.twitch.configs.TwitchReferenceService;
import com.funixproductions.api.service.twitch.reference.clients.game.TwitchReferenceGameClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwitchReferenceGameService extends TwitchReferenceService implements TwitchReferenceGameClient {

    private final TwitchReferenceGameClient client;

    @Override
    public TwitchDataResponseDTO<TwitchGameDTO> getGameInfoByName(String twitchAccessToken, String name) {
        try {
            return client.getGameInfoByName(super.addBearerPrefix(twitchAccessToken), name);
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchGameDTO> getGameInfoById(String twitchAccessToken, String id) {
        try {
            return client.getGameInfoById(super.addBearerPrefix(twitchAccessToken), id);
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }
}
