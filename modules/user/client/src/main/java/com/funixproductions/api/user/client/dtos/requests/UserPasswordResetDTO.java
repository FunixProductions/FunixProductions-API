package com.funixproductions.api.user.client.dtos.requests;

import com.funixproductions.api.user.client.dtos.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordResetDTO {

    @NotBlank(message = "Le token de réinitialisation ne peut pas être vide")
    private String resetToken;

    @NotBlank(message = "Le nouveau mot de passe ne peut pas être vide")
    @Size(min = 8, message = "Le nouveau mot de passe doit contenir au moins 8 caractères")
    @Pattern(regexp = UserDTO.REGEX_PASSWORD, message = "Le nouveau mot de passe doit contenir au moins 2 chiffres et 3 lettres")
    private String newPassword;

    @NotBlank(message = "Le nouveau mot de passe de confirmation ne peut pas être vide")
    @Size(min = 8, message = "Le nouveau mot de passe de confirmation doit contenir au moins 8 caractères")
    @Pattern(regexp = UserDTO.REGEX_PASSWORD, message = "Le nouveau mot de passe de confirmation doit contenir au moins 2 chiffres et 3 lettres")
    private String newPasswordConfirmation;

}
