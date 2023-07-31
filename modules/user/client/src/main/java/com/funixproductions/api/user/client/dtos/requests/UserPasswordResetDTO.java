package com.funixproductions.api.user.client.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordResetDTO {

    @NotBlank
    private String resetToken;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String newPasswordConfirmation;

}
