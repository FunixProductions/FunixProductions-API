package com.funixproductions.api.user.client.dtos.requests;

import com.funixproductions.api.user.client.dtos.UserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreationDTO {
    @NotBlank(message = "Le nom d'utilisateur ne peut pas être vide")
    @Size(min = 3, max = 40, message = "Le nom d'utilisateur doit contenir entre 3 et 40 caractères")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,}$", message = "Le nom d'utilisateur ne peut contenir que des lettres, des chiffres et les caractères _ -")
    private String username;

    @NotBlank(message = "L'adresse email ne peut pas être vide")
    @Email(message = "L'adresse email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(regexp = UserDTO.REGEX_PASSWORD, message = "Le mot de passe doit contenir au moins 2 chiffres et 3 lettres")
    private String password;

    @NotBlank(message = "Le mot de passe de confirmation ne peut pas être vide")
    @Size(min = 8, message = "Le mot de passe de confirmation doit contenir au moins 8 caractères")
    @Pattern(regexp = UserDTO.REGEX_PASSWORD, message = "Le mot de passe de confirmation doit contenir au moins 2 chiffres et 3 lettres")
    private String passwordConfirmation;

    @NotNull(message = "Vous devez accepter les CGV")
    @AssertTrue(message = "Vous devez accepter les CGV")
    private Boolean acceptCGV;

    @NotNull(message = "Vous devez accepter les CGU")
    @AssertTrue(message = "Vous devez accepter les CGU")
    private Boolean acceptCGU;

    @Valid
    @NotNull(message = "Le pays ne peut pas être vide")
    private UserDTO.Country country;
}
