package com.funixproductions.api.user.client.dtos.requests;

import com.funixproductions.api.user.client.dtos.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDTO extends UserDTO {

    private String oldPassword;

    private String newPassword;

    private String newPasswordConfirmation;

}
