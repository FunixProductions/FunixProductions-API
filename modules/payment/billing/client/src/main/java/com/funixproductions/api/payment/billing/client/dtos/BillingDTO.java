package com.funixproductions.api.payment.billing.client.dtos;

import com.funixproductions.api.payment.billing.client.enums.PaymentOrigin;
import com.funixproductions.api.payment.billing.client.enums.PaymentType;
import com.funixproductions.core.crud.dtos.ApiDTO;
import com.funixproductions.core.tools.pdf.entities.PDFCompanyData;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class BillingDTO extends ApiDTO {

    @NotBlank(message = "La description de la facture est obligatoire")
    @Size(max = 300, message = "La description de la facture ne doit pas dépasser 300 caractères")
    private String billingDescription;

    @NotNull(message = "Le type de paiement est obligatoire")
    private PaymentType paymentType;

    @Valid
    @NotNull(message = "L'entité facturée est obligatoire")
    private BilledEntity billedEntity;

    @NotNull(message = "L'origine du paiement est obligatoire")
    private PaymentOrigin paymentOrigin;

    /**
     * Amount total of the invoice
     */
    @Valid
    @NotNull(message = "Le montant total est obligatoire")
    private Price amountTotal;

    /**
     * TVA information, null if no tax
     */
    private VATInformation vatInformation;

    @Valid
    @NotNull(message = "Les objets facturés sont obligatoires")
    @Size(min = 1, message = "Les objets facturés sont obligatoires")
    private List<@Valid BillingObjectDTO> billingObjects;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BilledEntity implements PDFCompanyData, Serializable {
        @NotBlank(message = "Le nom de l'entité facturée est obligatoire")
        private String name;

        @Nullable
        private String address;

        @Nullable
        private String zipCode;

        @Nullable
        private String city;

        @Nullable
        private String phone;

        @NotBlank(message = "L'email de l'entité facturée est obligatoire")
        @Email(message = "L'email de l'entité facturée est invalide")
        private String email;

        @Nullable
        private String website;

        @Nullable
        private String siret;

        @Nullable
        private String tvaCode;

        @Nullable
        private String userFunixProdId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Price implements Serializable {
        @NotNull(message = "Le prix HT est obligatoire")
        @Min(value = 0, message = "Le prix HT doit être supérieur ou égal à 0")
        private Double ht;

        @NotNull(message = "Le cout de la TAX est obligatoire")
        @Min(value = 0, message = "Le cout de la TAX doit être supérieur ou égal à 0")
        private Double tax;

        @NotNull(message = "Le prix TTC est obligatoire")
        @Min(value = 0, message = "Le prix TTC doit être supérieur ou égal à 0")
        private Double ttc;

        @Nullable
        @Min(value = 0, message = "Le prix de la réduction doit être supérieur ou égal à 0")
        private Double discount;

        public Price(@NonNull Double ht, @Nullable Double tax) {
            this.ht = ht;
            this.tax = tax == null ? 0 : tax;
            this.ttc = ht + (tax == null ? 0 : tax);
            this.discount = null;
        }

        public Price(@NonNull Double ht) {
            this.ht = ht;
            this.tax = 0.0;
            this.ttc = ht;
            this.discount = null;
        }
    }
}
