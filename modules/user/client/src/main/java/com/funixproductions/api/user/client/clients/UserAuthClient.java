package com.funixproductions.api.user.client.clients;

import com.funixproductions.api.user.client.dtos.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "UserAuth",
        url = "${funixproductions.api.user.app-domain-url}",
        path = "/user/auth/"
)
public interface UserAuthClient {

    @GetMapping("current")
    UserDTO current(@RequestHeader("Authorization") String token);

}
