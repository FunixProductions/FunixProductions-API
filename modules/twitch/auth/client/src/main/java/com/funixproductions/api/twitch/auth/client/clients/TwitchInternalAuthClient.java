package com.funixproductions.api.twitch.auth.client.clients;

import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchInternalAuthClient",
        url = "${funixproductions.api.twitch.auth.app-domain-url}",
        path = "/kubeinternal/twitch/auth/"
)
public interface TwitchInternalAuthClient {

    @GetMapping("client")
    TwitchClientTokenDTO fetchToken(
            @RequestParam String userUUID,
            @RequestParam(defaultValue = "VIEWER") @NonNull String tokenType
    );

    @GetMapping("clientByStreamerName")
    TwitchClientTokenDTO fetchTokenByStreamerName(
            @RequestParam String streamerName,
            @RequestParam(defaultValue = "VIEWER") @NonNull String tokenType
    );

    @GetMapping("clientByStreamerId")
    TwitchClientTokenDTO fetchTokenByStreamerId(
            @RequestParam String streamerId,
            @RequestParam(defaultValue = "VIEWER") @NonNull String tokenType
    );

    @GetMapping("server")
    String fetchServerToken();

}
