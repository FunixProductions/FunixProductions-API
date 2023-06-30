package com.funixproductions.api.twitch.reference.service.resources.game;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.reference.client.clients.game.TwitchGameClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.game.TwitchGameDTO;
import com.funixproductions.api.twitch.reference.service.resources.TwitchReferenceResource;
import com.funixproductions.api.twitch.reference.service.services.game.TwitchReferenceGameService;
import com.funixproductions.api.user.client.security.CurrentSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/game")
public class TwitchGameResource extends TwitchReferenceResource implements TwitchGameClient {

    private final TwitchReferenceGameService gameService;

    public TwitchGameResource(TwitchInternalAuthClient tokenService,
                              CurrentSession currentSession,
                              TwitchReferenceGameService gameService) {
        super(tokenService, currentSession);
        this.gameService = gameService;
    }

    @Override
    public TwitchDataResponseDTO<TwitchGameDTO> getGameByName(String name) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        return gameService.getGameInfoByName(tokenDTO.getAccessToken(), name);
    }

    @Override
    public TwitchDataResponseDTO<TwitchGameDTO> getGameById(String id) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        return gameService.getGameInfoById(tokenDTO.getAccessToken(), id);
    }
}
