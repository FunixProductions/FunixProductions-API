package com.funixproductions.api.payment.paypal.client.dtos.requests.card;

import com.funixproductions.api.payment.paypal.client.dtos.requests.PaymentDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditCardPaymentDTO extends PaymentDTO {

    @NotBlank(message = "Le nom du titulaire de la carte est obligatoire")
    private String cardHolderName;

    @NotBlank(message = "Le numéro de la carte est obligatoire")
    private String cardNumber;

    @NotBlank(message = "Le code de sécurité de la carte est obligatoire")
    private String securityCode;

    @NotNull(message = "Le mois d'expiration de la carte est obligatoire")
    private Integer expirationMonth;

    @NotNull(message = "L'année d'expiration de la carte est obligatoire")
    private Integer expirationYear;

    @NotNull(message = "L'adresse de facturation est obligatoire")
    private BillingAddressDTO billingAddress;

    @Getter
    @Setter
    public static class BillingAddressDTO {
        @NotBlank(message = "L'adresse est obligatoire")
        private String address;

        @NotBlank(message = "La ville est obligatoire")
        private String city;

        @NotBlank(message = "Le code postal est obligatoire")
        private String postalCode;

        @NotBlank(message = "L'état est obligatoire")
        private String state;

        @NotBlank(message = "Le code pays est obligatoire")
        private String countryCode;
    }

}
