package com.funixproductions.api.twitch.reference.client.clients.game;

import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.game.TwitchGameDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchGameClient",
        url = "${funixproductions.api.twitch.reference.app-domain-url}",
        path = "/kubeinternal/twitch/game"
)
public interface TwitchGameClient {

    @GetMapping("name")
    TwitchDataResponseDTO<TwitchGameDTO> getGameByName(@RequestParam(name = "name") String name);

    @GetMapping("id")
    TwitchDataResponseDTO<TwitchGameDTO> getGameById(@RequestParam(name = "id") String id);

}
