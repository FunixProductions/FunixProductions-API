package com.funixproductions.api.payment.paypal.client.dtos.requests;

import com.funixproductions.core.tools.pdf.tools.VATInformation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public abstract class PaymentDTO {

    @NotBlank(message = "L'URL d'annulation est obligatoire")
    private String cancelUrl;

    @NotBlank(message = "L'URL de retour valide est obligatoire")
    private String returnUrl;

    @Nullable
    private UserPaymentDTO user;

    @NotNull(message = "Les unités d'achat sont obligatoires")
    private List<PurchaseUnitDTO> purchaseUnits;

    @NotBlank(message = "L'origine de la requête est obligatoire")
    private String originRequest;

    @Getter
    @Setter
    public static class UserPaymentDTO {
        @NotNull(message = "L'ID de l'utilisateur est obligatoire")
        private UUID userId;

        @NotNull(message = "L'email de l'utilisateur est obligatoire")
        private String userEmail;

        @NotNull(message = "Le nom d'utilisateur est obligatoire")
        private String username;
    }

    @Getter
    @Setter
    public static class PurchaseUnitDTO {
        /**
         * The API caller-provided external ID for the purchase unit. Required for multiple purchase units when you must update the order through PATCH. If you omit this value and the order contains only one purchase unit, PayPal sets this value to default.
         */
        @Nullable
        private String referenceId;

        /**
         * The API caller-provided external ID. Used to reconcile client transactions with PayPal transactions. Appears in transaction and settlement reports but is not visible to the payer.
         */
        @Nullable
        private String customId;

        /**
         * The soft descriptor is the dynamic text used to construct the statement descriptor that appears on a payer's card statement.<br>
         * If an Order is paid using the "PayPal Wallet", the statement descriptor will appear in following format on the payer's card statement: PAYPAL_prefix+(space)+merchant_descriptor+(space)+ soft_descriptor
         */
        @Nullable
        private String softDescriptor;

        /**
         * The purchase description. The maximum length of the character is dependent on the type of characters used. The character length is specified assuming a US ASCII character. Depending on type of character; (e.g. accented character, Japanese characters) the number of characters that that can be specified as input might not equal the permissible max length.
         */
        @NotBlank(message = "La description de l'achat est obligatoire")
        @Max(value = 127, message = "La description de l'achat ne doit pas dépasser 127 caractères")
        private String description;

        /**
         * An array of items that the customer purchases from the merchant.
         */
        @NotNull(message = "Les articles de l'achat sont obligatoires")
        private List<Item> items;

        @Getter
        @Setter
        public static class Item {
            /**
             * The item name or title. The character length is 127 single-byte alphanumeric characters
             */
            @NotBlank(message = "Le nom de l'article est obligatoire")
            @Max(value = 127, message = "Le nom de l'article ne doit pas dépasser 127 caractères")
            private String name;

            /**
             * The item quantity. Must be a whole number.
             */
            @NotNull(message = "La quantité de l'article est obligatoire")
            private Integer quantity;

            /**
             * The item description. The character length is 127 single-byte alphanumeric characters
             */
            @NotBlank(message = "La description de l'article est obligatoire")
            @Max(value = 127, message = "La description de l'article ne doit pas dépasser 127 caractères")
            private String description;

            /**
             * The item price or rate per unit. Must be a valid positive number. For example, "12.34" to indicate twelve dollars and thirty-four cents.
             */
            @NotNull(message = "Le prix de l'article est obligatoire")
            private Double price;

            /**
             * Tax info, null if no tax
             */
            @Nullable
            private VATInformation vatInformation;
        }

    }
}
