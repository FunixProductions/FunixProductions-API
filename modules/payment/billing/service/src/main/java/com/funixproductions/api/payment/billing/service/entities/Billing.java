package com.funixproductions.api.payment.billing.service.entities;

import com.funixproductions.api.payment.billing.client.enums.PaymentOrigin;
import com.funixproductions.api.payment.billing.client.enums.PaymentType;
import com.funixproductions.core.crud.entities.ApiEntity;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "funixproductions_billing")
public class Billing extends ApiEntity {

    @Column(nullable = false, length = 300, name = "billing_description")
    private String billingDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "payment_type")
    private PaymentType paymentType;

    @Column(nullable = false, name = "billed_entity_name")
    private String billedEntityName;

    @Column(name = "billed_entity_address")
    private String billedEntityAddress;

    @Column(name = "billed_entity_zip_code")
    private String billedEntityZipCode;

    @Column(name = "billed_entity_city")
    private String billedEntityCity;

    @Column(name = "billed_entity_phone")
    private String billedEntityPhone;

    @Column(nullable = false, name = "billed_entity_email")
    private String billedEntityEmail;

    @Column(name = "billed_entity_website")
    private String billedEntityWebsite;

    @Column(name = "billed_entity_siret")
    private String billedEntitySiret;

    @Column(name = "billed_entity_tva_number")
    private String billedEntityTvaNumber;

    @Column(name = "billed_entity_funix_prod_id")
    private String billedEntityFunixProdId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_origin", nullable = false)
    private PaymentOrigin paymentOrigin;

    @Column(name = "percentage_discount")
    private Double percentageDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "vat_information")
    private VATInformation vatInformation;

    @Column(name = "price_ht", nullable = false)
    private Double priceHT;

    @Column(name = "price_tax", nullable = false)
    private Double priceTax;

    @Column(name = "price_ttc", nullable = false)
    private Double priceTTC;

    @Column(name = "invoice_file_path")
    private String invoiceFilePath;

    @PreRemove
    void preRemove() throws ApiBadRequestException {
        throw new ApiBadRequestException("Impossible de supprimer une facture.");
    }

}
