package com.funixproductions.api.accounting.service.entities;

import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.client.enums.PaymentOrigin;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class AccountingReport {

    /**
     * Chiffre d'affaire en hors taxe
     */
    private final Double caHtGained;

    /**
     * La TVA à déclarer par pays membre de l'union européenne
     */
    private final Map<VATInformation, Double> tvaToDeclare;

    /**
     * La TVA à déduire des achats (facture orange, hébergement web et dédié, etc...)
     */
    private final Double tvaToDeduct;

    /**
     * Le chiffre d'affaire en hors taxe par origine de paiement (pacifista, funixgaming, etc...)
     */
    private final Map<PaymentOrigin, Double> caHtByPaymentOrigin;

    public AccountingReport(final List<BillingDTO> billingData, final List<Product> products) {
        this.tvaToDeclare = new HashMap<>();

        for (VATInformation vatInformation : VATInformation.values()) {
            this.tvaToDeclare.put(vatInformation, 0.0);
        }
    }
}
