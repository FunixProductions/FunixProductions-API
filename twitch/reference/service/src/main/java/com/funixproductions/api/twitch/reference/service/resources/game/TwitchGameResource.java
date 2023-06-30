package com.funixproductions.api.twitch.reference.service.resources.game;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.reference.client.clients.game.TwitchGameClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.game.TwitchGameDTO;
import com.funixproductions.api.twitch.reference.service.services.game.TwitchReferenceGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/reference/game")
@RequiredArgsConstructor
public class TwitchGameResource implements TwitchGameClient {

    private final TwitchReferenceGameService gameService;
    private final TwitchInternalAuthClient internalAuthClient;

    @Override
    public TwitchDataResponseDTO<TwitchGameDTO> getGameByName(String name) {
        return gameService.getGameInfoByName(this.internalAuthClient.fetchServerToken(), name);
    }

    @Override
    public TwitchDataResponseDTO<TwitchGameDTO> getGameById(String id) {
        return gameService.getGameInfoById(this.internalAuthClient.fetchServerToken(), id);
    }
}
