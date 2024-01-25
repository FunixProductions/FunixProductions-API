package com.funixproductions.api.user.client.dtos;

import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.core.crud.dtos.ApiDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class UserDTO extends ApiDTO {
    public static final String REGEX_PASSWORD = "^(?=.*\\d.*\\d)(?=.*[a-zA-Z].*[a-zA-Z].*[a-zA-Z]).*$";
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_-]{3,}$";

    @NotBlank(message = "Le nom d'utilisateur ne peut pas être vide")
    @Size(min = 3, max = 40, message = "Le nom d'utilisateur doit contenir entre 3 et 40 caractères")
    @Pattern(regexp = REGEX_USERNAME, message = "Le nom d'utilisateur ne peut contenir que des lettres, des chiffres et les caractères _ -")
    private String username;

    @NotBlank(message = "L'adresse email ne peut pas être vide")
    @Email(message = "L'adresse email doit être valide")
    private String email;

    @NotNull(message = "Le rôle ne peut pas être vide")
    private UserRole role = UserRole.USER;

    @NotNull(message = "Le compte doit être validé ou non")
    private Boolean valid = false;

    @Valid
    @NotNull(message = "Le pays ne peut pas être vide")
    private Country country;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Country {
        @NotBlank(message = "Le nom du pays ne peut pas être vide")
        private String name;

        @NotNull(message = "Le code du pays ne peut pas être vide")
        @Min(value = 1, message = "Le code du pays doit être supérieur à 0")
        private Integer code;

        @NotBlank(message = "Le code à 2 caractères du pays ne peut pas être vide")
        @Size(min = 2, max = 2, message = "Le code à 2 caractères du pays doit contenir 2 caractères")
        private String countryCode2Chars;

        @NotBlank(message = "Le code à 3 caractères du pays ne peut pas être vide")
        @Size(min = 3, max = 3, message = "Le code à 3 caractères du pays doit contenir 3 caractères")
        private String countryCode3Chars;
    }
}
