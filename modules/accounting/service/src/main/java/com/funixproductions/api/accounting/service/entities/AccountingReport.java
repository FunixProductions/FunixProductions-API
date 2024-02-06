package com.funixproductions.api.accounting.service.entities;

import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import lombok.Getter;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Getter
public class AccountingReport {

    /**
     * Chiffre d'affaire en hors taxe percu (pour l'URSAFF)
     */
    private final Double caHtTotal;

    /**
     * Chiffre d'affaire en hors taxe de prestation de services EN FRANCE et hors EU (revenus liés à pacifista, funixgaming les dons, etc...)
     * Pour Impots.gouv case A1
     */
    private final Double caHtPrestation;

    /**
     * Si tu achètes un service à une entreprise située dans l’UE (abonnement, logiciel, etc.) déclaration HT
     * Case A3 sur impots.gouv et case 17
     */
    private final Double htServiceBoughtEu;

    /**
     * Si tu achètes un service à une entreprise située hors UE déclaration HT
     * Case A2 sur impots.gouv
     */
    private final Double htServiceBoughtNonEu;

    /**
     * Si tu achètes un bien physique à une entreprise située dans l’UE (micro, ordi etc) déclaration HT
     * Case B2 sur impots.gouv et case 17
     */
    private final Double htPhysicalProductBoughtEu;

    /**
     * Si tu achètes un bien physique à une entreprise située hors UE déclaration HT
     * Case A4 sur impots.gouv
     */
    private final Double htPhysicalProductBoughtNonEu;

    /**
     * La TVA à déclarer par pays membre de l'union européenne, ce sont les ventes de produits réalisés en taxe tva
     * C'est la case E3 de tous les pays de l'UE hors France (si CA annuel > 10 000€)
     * Si tu fais plus de 10 000 € annuel de CA dans l’UE (hors France) à des clients particuliers
     */
    private final Map<VATInformation, Double> htPrestationSoldEu;

    /**
     * La TVA (sûrement 20 %) sur le montant HT de la facture de ton sous-traitant ou abonnement/logiciel dont l’entreprise est basée en UE (hors France)
     * Ligne 17 qui le montant TVA des achats réalisées B2B dans l'UE
     */
    private final Double intracomVat;

    /**
     * Opérations réalisées en France Métropolitaine
     */
    private final Double francePrestationTaxHt;

    /**
     * La TVA à déduire des achats (facture orange, hébergement web et dédié, etc...)
     * Ligne 20
     */
    private final Double tvaToDeduct;

    public AccountingReport(final List<BillingDTO> billingData, final List<Product> products, final List<Income> incomes) {
        this.caHtTotal = calculateCaHtTotal(billingData, incomes);
        this.caHtPrestation = calculateCaHtPrestation(billingData);
        this.htServiceBoughtEu = calculateHtServiceBoughtEu(products);
        this.htServiceBoughtNonEu = calculateHtServiceBoughtNonEu(products);
        this.htPhysicalProductBoughtEu = calculateHtPhysicalProductBoughtEu(products);
        this.htPhysicalProductBoughtNonEu = calculateHtPhysicalProductBoughtNonEu(products);
        this.htPrestationSoldEu = calculateHtPrestationSoldEu(billingData);
        this.francePrestationTaxHt = calculateFranceTvaHt(billingData);
        this.intracomVat = this.htServiceBoughtEu * 0.2 + this.htPhysicalProductBoughtEu * 0.2;
        this.tvaToDeduct = calculateTvaToDeduct(products);
    }

    private static Double calculateCaHtTotal(final List<BillingDTO> billingData, final List<Income> incomes) {
        Double ca = 0.0;

        for (final BillingDTO billingDTO : billingData) {
            ca += billingDTO.getAmountTotal().getHt();
        }
        for (final Income income : incomes) {
            ca += income.getAmount();
        }
        return ca;
    }

    private static Double calculateCaHtPrestation(final List<BillingDTO> billingData) {
        Double ca = 0.0;

        for (final BillingDTO billingDTO : billingData) {
            ca += billingDTO.getAmountTotal().getHt();
        }
        return ca;
    }

    private static Double calculateFranceTvaHt(final List<BillingDTO> billingData) {
        Double tva = 0.0;

        for (final BillingDTO billingDTO : billingData) {
            if (billingDTO.getVatInformation() == null || billingDTO.getVatInformation() == VATInformation.FRANCE) {
                tva += billingDTO.getAmountTotal().getHt();
            }
        }
        return tva;
    }

    private static Double calculateHtServiceBoughtEu(final List<Product> products) {
        Double boughtHt = 0.0;

        for (final Product product : products) {
            if (product.getIsEu() && !product.getIsPhysical()) {
                boughtHt += product.getAmountHT();
            }
        }
        return boughtHt;
    }

    private static Double calculateHtServiceBoughtNonEu(final List<Product> products) {
        Double boughtHt = 0.0;

        for (final Product product : products) {
            if (!product.getIsEu() && !product.getIsPhysical()) {
                boughtHt += product.getAmountHT();
            }
        }
        return boughtHt;
    }

    private static Double calculateHtPhysicalProductBoughtEu(final List<Product> products) {
        Double boughtHt = 0.0;

        for (final Product product : products) {
            if (product.getIsEu() && product.getIsPhysical()) {
                boughtHt += product.getAmountHT();
            }
        }
        return boughtHt;
    }

    private static Double calculateHtPhysicalProductBoughtNonEu(final List<Product> products) {
        Double boughtHt = 0.0;

        for (final Product product : products) {
            if (!product.getIsEu() && product.getIsPhysical()) {
                boughtHt += product.getAmountHT();
            }
        }
        return boughtHt;
    }

    private static Map<VATInformation, Double> calculateHtPrestationSoldEu(final List<BillingDTO> billingData) {
        final Map<VATInformation, Double> htEu = new EnumMap<>(VATInformation.class);

        for (final BillingDTO billingDTO : billingData) {
            if (billingDTO.getVatInformation() != null && billingDTO.getVatInformation() != VATInformation.FRANCE) {
                htEu.put(
                        billingDTO.getVatInformation(),
                        htEu.getOrDefault(billingDTO.getVatInformation(), 0.0) + billingDTO.getAmountTotal().getHt());
            }
        }
        return htEu;
    }

    private static Double calculateTvaToDeduct(final List<Product> products) {
        Double tva = 0.0;

        for (final Product product : products) {
            tva += product.getAmountTax();
        }
        return tva;
    }
}
