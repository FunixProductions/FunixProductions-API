package com.funixproductions.api.user.client.dtos.requests;

import com.funixproductions.api.core.enums.FrontOrigins;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordResetRequestDTO {

    @NotBlank
    private String email;

    @NotNull
    private FrontOrigins origin;

}
