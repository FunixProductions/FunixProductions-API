package com.funixproductions.api.user.client.dtos;

import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.core.crud.dtos.ApiDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private UserRole role;

    @NotNull(message = "Le compte doit être validé ou non")
    private Boolean valid = false;

    @Valid
    @NotNull(message = "Le pays ne peut pas être vide")
    private Country country;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof final UserDTO userDTO) {
            return this.username.equals(userDTO.username) &&
                    this.email.equals(userDTO.email) &&
                    this.role.equals(userDTO.role) &&
                    this.valid.equals(userDTO.valid) &&
                    this.country.equals(userDTO.country) &&
                    super.equals(obj);
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() +
                this.username.hashCode() +
                this.email.hashCode() +
                this.role.hashCode() +
                this.valid.hashCode() +
                this.country.hashCode();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Country implements Serializable {
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

        @Override
        public int hashCode() {
            return this.name.hashCode() +
                    this.code.hashCode() +
                    this.countryCode2Chars.hashCode() +
                    this.countryCode3Chars.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof final Country country) {
                return this.name.equals(country.name) &&
                        this.code.equals(country.code) &&
                        this.countryCode2Chars.equals(country.countryCode2Chars) &&
                        this.countryCode3Chars.equals(country.countryCode3Chars);
            } else {
                return false;
            }
        }
    }

    /**
     * Generate fake data for testing purposes, DO NOT USE FOR REAL / PRODUCTION USAGE
     * @return UserDTO with fake data
     */
    public static UserDTO generateFakeDataForTestingPurposes() {
        final UserDTO userDTO = new UserDTO();

        userDTO.setUsername(UUID.randomUUID().toString());
        userDTO.setEmail(UUID.randomUUID() + "@fakemail.com");
        userDTO.setRole(UserRole.USER);
        userDTO.setValid(false);
        userDTO.setId(UUID.randomUUID());
        userDTO.setCreatedAt(new Date());
        userDTO.setUpdatedAt(new Date());

        final Country country = new Country();
        country.setName("France");
        country.setCode(33);
        country.setCountryCode2Chars("fr");
        country.setCountryCode3Chars("fra");

        userDTO.setCountry(country);
        return userDTO;
    }
}
