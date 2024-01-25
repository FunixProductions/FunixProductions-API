package com.funixproductions.api.user.client.dtos.requests;

import com.funixproductions.api.user.client.dtos.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {
    @NotBlank(message = "Le nom d'utilisateur ne peut pas être vide")
    @Size(min = 3, max = 40, message = "Le nom d'utilisateur doit contenir entre 3 et 40 caractères")
    @Pattern(regexp = UserDTO.REGEX_USERNAME, message = "Le nom d'utilisateur ne peut contenir que des lettres, des chiffres et les caractères _ -")
    private String username;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    private String password;

    private Boolean stayConnected = false;
}
