package com.funixproductions.api.client.twitch.auth.clients;

import com.funixproductions.api.client.core.config.FeignConfig;
import com.funixproductions.api.client.twitch.auth.dtos.TwitchClientTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchAuthClient",
        url = "${funixproductions.api.app-domain-url}",
        path = "/twitch/auth/",
        configuration = FeignConfig.class
)
public interface TwitchAuthClient {

    @GetMapping("clientAuthUrl")
    String getAuthClientUrl(@RequestParam(defaultValue = "VIEWER") String tokenType);

    @GetMapping("accessToken")
    TwitchClientTokenDTO getAccessToken();

}
