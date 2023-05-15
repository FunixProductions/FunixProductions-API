package com.funixproductions.api.client.user.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private Boolean stayConnected;
}
