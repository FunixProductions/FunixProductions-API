package com.funixproductions.api.twitch.reference.service.clients.users;

import com.funixproductions.api.twitch.auth.client.configurations.TwitchApiRequestInterceptor;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "TwitchReferenceUsersClient",
        url = "https://api.twitch.tv",
        configuration = TwitchApiRequestInterceptor.class,
        path = "helix/users"
)
public interface TwitchReferenceUsersClient {

    /**
     * Gets information about one or more users.
     * To include the user’s verified email address in the response, you must use a user access token that includes the user:read:email scope.
     * @param twitchAccessToken Requires an app access token or user access token.
     * @param name name list to get
     * @return users
     */
    @GetMapping
    TwitchDataResponseDTO<TwitchUserDTO> getUsersByName(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                        @RequestParam(name = "login") List<String> name);

    /**
     * Gets information about one or more users.
     * To include the user’s verified email address in the response, you must use a user access token that includes the user:read:email scope.
     * @param twitchAccessToken Requires an app access token or user access token.
     * @param id id list to get
     * @return users
     */
    @GetMapping
    TwitchDataResponseDTO<TwitchUserDTO> getUsersById(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                      @RequestParam(name = "id") List<String> id);


}
