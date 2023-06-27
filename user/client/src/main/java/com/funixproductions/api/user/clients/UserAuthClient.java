package com.funixproductions.api.user.clients;

import com.funixproductions.api.client.user.dtos.UserDTO;
import com.funixproductions.api.client.user.dtos.UserTokenDTO;
import com.funixproductions.api.client.user.dtos.requests.UserCreationDTO;
import com.funixproductions.api.client.user.dtos.requests.UserLoginDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "UserAuth",
        url = "${funixproductions.api.app-domain-url}",
        path = "/user/auth/"
)
public interface UserAuthClient {

    @PostMapping("register")
    UserDTO register(@RequestBody @Valid UserCreationDTO request, @RequestHeader("google_reCaptcha") @Nullable String captchaCode);

    @PostMapping("login")
    UserTokenDTO login(@RequestBody @Valid UserLoginDTO request, @RequestHeader("google_reCaptcha") @Nullable String captchaCode);

    @GetMapping("current")
    UserDTO current(@RequestHeader("Authorization") String token);

}
