package com.funixproductions.api.twitch.auth.client.clients;

import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchAuthClient",
        url = "${funixproductions.api.twitch.auth.app-domain-url}",
        path = "/twitch/auth/"
)
public interface TwitchAuthClient {

    @GetMapping("clientAuthUrl")
    String getAuthClientUrl(@RequestParam(defaultValue = "VIEWER") String tokenType);

    @GetMapping("accessToken")
    TwitchClientTokenDTO getAccessToken();

}
