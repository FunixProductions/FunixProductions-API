package com.funixproductions.api.payment.paypal.client.dtos.requests.card;

import com.funixproductions.api.payment.paypal.client.dtos.requests.PaymentDTO;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditCardPaymentDTO extends PaymentDTO {

    @NotBlank(message = "Le nom du titulaire de la carte est obligatoire")
    @Size(min = 1, max = 300, message = "Le nom du titulaire de la carte est invalide")
    private String cardHolderName;

    @NotBlank(message = "Le numéro de la carte est obligatoire")
    @Size(min = 13, max = 19, message = "Le numéro de la carte est invalide")
    private String cardNumber;

    @NotBlank(message = "Le code de sécurité de la carte est obligatoire")
    @Size(min = 3, max = 4, message = "Le code de sécurité de la carte est invalide")
    private String securityCode;

    @NotNull(message = "Le mois d'expiration de la carte est obligatoire")
    @Max(value = 12, message = "Le mois d'expiration de la carte est invalide")
    @Min(value = 1, message = "Le mois d'expiration de la carte est invalide")
    private Integer expirationMonth;

    @NotNull(message = "L'année d'expiration de la carte est obligatoire")
    @Max(value = 2200, message = "L'année d'expiration de la carte est invalide")
    @Min(value = 2024, message = "L'année d'expiration de la carte est invalide")
    private Integer expirationYear;

}
