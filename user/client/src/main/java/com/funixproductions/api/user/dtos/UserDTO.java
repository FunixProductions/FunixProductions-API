package com.funixproductions.api.user.dtos;

import com.funixproductions.api.user.enums.UserRole;
import com.funixproductions.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO extends ApiDTO {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private UserRole role;
}
