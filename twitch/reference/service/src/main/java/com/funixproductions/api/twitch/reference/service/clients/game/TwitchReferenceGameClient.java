package com.funixproductions.api.twitch.reference.service.clients.game;

import com.funixproductions.api.client.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.client.twitch.reference.dtos.responses.game.TwitchGameDTO;
import com.funixproductions.api.service.twitch.configs.TwitchReferenceRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchReferenceGameClient",
        url = "${twitch.api.app-api-domain-url}",
        configuration = TwitchReferenceRequestInterceptor.class,
        path = "helix/games"
)
public interface TwitchReferenceGameClient {

    @GetMapping
    TwitchDataResponseDTO<TwitchGameDTO> getGameInfoByName(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                           @RequestParam(name = "name") String name);

    @GetMapping
    TwitchDataResponseDTO<TwitchGameDTO> getGameInfoById(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                         @RequestParam(name = "id") String id);

}