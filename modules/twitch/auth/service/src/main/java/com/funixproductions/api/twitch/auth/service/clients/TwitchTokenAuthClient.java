package com.funixproductions.api.twitch.auth.service.clients;

import com.funixproductions.api.twitch.auth.service.dtos.TwitchTokenResponseDTO;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(
        name = "TwitchTokenAuthClient",
        url = "https://id.twitch.tv",
        path = "oauth2"
)
public interface TwitchTokenAuthClient {

    /**
     * <a href="https://dev.twitch.tv/docs/authentication/getting-tokens-oauth#client-credentials-grant-flow">Documentation</a>
     * @param formParams map where you need to have client_id client_secret grant_type
     * @return access token
     */
    @PostMapping("token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TwitchTokenResponseDTO getToken(Map<String, String> formParams);

}
