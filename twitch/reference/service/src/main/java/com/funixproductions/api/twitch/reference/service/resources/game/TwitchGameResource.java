package com.funixproductions.api.twitch.reference.service.resources.game;

import com.funixproductions.api.client.twitch.auth.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.client.twitch.reference.clients.game.TwitchGameClient;
import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.game.TwitchGameDTO;
import com.funixproductions.api.service.twitch.auth.services.TwitchClientTokenService;
import com.funixproductions.api.service.twitch.reference.resources.TwitchReferenceResource;
import com.funixproductions.api.service.twitch.reference.services.game.TwitchReferenceGameService;
import com.funixproductions.api.service.user.services.CurrentSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/game")
public class TwitchGameResource extends TwitchReferenceResource implements TwitchGameClient {

    private final TwitchReferenceGameService gameService;

    public TwitchGameResource(TwitchClientTokenService tokenService,
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
