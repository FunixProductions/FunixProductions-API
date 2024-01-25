package com.funixproductions.api.user.client.dtos.requests;

import com.funixproductions.api.core.enums.FrontOrigins;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordResetRequestDTO {

    @Email(message = "L'adresse email doit être valide")
    @NotBlank(message = "L'adresse email ne peut pas être vide")
    private String email;

    @NotNull(message = "L'origine de la requête ne peut pas être vide")
    private FrontOrigins origin;

}
