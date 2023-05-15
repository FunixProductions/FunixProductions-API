package com.funixproductions.api.client.user.dtos.requests;

import com.funixproductions.api.client.user.dtos.UserDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSecretsDTO extends UserDTO {
    @NotBlank
    private String password;
}
