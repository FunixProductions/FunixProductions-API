package com.funixproductions.api.twitch.reference.client.clients.users;

import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "TwitchUsersClient",
        url = "${funixproductions.api.twitch.reference.app-domain-url}",
        path = "/twitch/reference/users"
)
public interface TwitchUsersClient {

    @GetMapping("usersByName")
    TwitchDataResponseDTO<TwitchUserDTO> getUsersByName(@RequestParam(name = "login") List<String> name);

    @GetMapping("usersById")
    TwitchDataResponseDTO<TwitchUserDTO> getUsersById(@RequestParam(name = "id") List<String> id);

}
