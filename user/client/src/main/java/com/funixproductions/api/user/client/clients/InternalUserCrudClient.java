package com.funixproductions.api.user.client.clients;

import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "UserCrudInternal",
        url = "${funixproductions.api.user.app-domain-url}",
        path = "/kubeinternal/user/"
)
public interface InternalUserCrudClient extends UserCrudClient {

    @PostMapping("accessToken")
    UserTokenDTO generateAccessToken(@RequestBody UserDTO userDTO);

}
