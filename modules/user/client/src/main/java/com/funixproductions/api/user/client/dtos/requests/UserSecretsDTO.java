package com.funixproductions.api.user.client.dtos.requests;

import com.funixproductions.api.user.client.dtos.UserDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSecretsDTO extends UserDTO {
    @NotBlank
    private String password;
}
