package com.funixproductions.api.user.client.dtos.requests;

import com.funixproductions.api.user.client.dtos.UserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDTO {

    @Size(min = 8, message = "L'ancien mot de passe doit contenir au moins 8 caractères")
    private String oldPassword;

    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(regexp = UserDTO.REGEX_PASSWORD, message = "Le mot de passe doit contenir au moins 2 chiffres et 3 lettres")
    private String newPassword;

    @Size(min = 8, message = "Le mot de passe de confirmation doit cotenir au moins 8 caractères")
    @Pattern(regexp = UserDTO.REGEX_PASSWORD, message = "Le nouveau mot de passe doit contenir au moins 2 chiffres et 3 lettres")
    private String newPasswordConfirmation;

    @Size(min = 3, max = 40, message = "Le nom d'utilisateur doit contenir entre 3 et 40 caractères")
    @Pattern(regexp = UserDTO.REGEX_USERNAME, message = "Le nom d'utilisateur ne peut contenir que des lettres, des chiffres et les caractères _ -")
    private String username;

    @Email(message = "L'adresse email doit être valide")
    private String email;

    @Valid
    private UserDTO.Country country;

}
