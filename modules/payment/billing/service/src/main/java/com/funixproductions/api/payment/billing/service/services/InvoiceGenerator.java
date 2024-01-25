package com.funixproductions.api.payment.billing.service.services;

import com.funixproductions.api.payment.billing.client.dtos.BillingObjectDTO;
import com.funixproductions.api.payment.billing.client.enums.PaymentOrigin;
import com.funixproductions.api.payment.billing.client.enums.PaymentType;
import com.funixproductions.api.payment.billing.service.entities.FunixProductionsCompanyData;
import com.funixproductions.core.tools.pdf.entities.PDFCompanyData;
import com.funixproductions.core.tools.pdf.generators.PDFGeneratorInvoice;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

public class InvoiceGenerator extends PDFGeneratorInvoice {

    protected InvoiceGenerator(@NonNull final FunixProductionsCompanyData funixproductions,
                               @NonNull final byte[] funixProdLogo,
                               @NonNull final List<BillingObjectDTO> items,
                               @NonNull final PDFCompanyData client,
                               @NonNull final String description,
                               @NonNull final PaymentType paymentType,
                               @NonNull final Long invoiceId,
                               @Nullable final PaymentOrigin paymentOrigin,
                               @Nullable final VATInformation vatInformation) {
        super("facture-funix-productions", funixproductions, funixProdLogo, "FunixProductions_Logo.png", items, client);

        super.setInvoiceDescription(description);
        super.setPaymentMethod(paymentType.name());
        super.setInvoiceNumber(generateInvoiceNumber(invoiceId));
        if (paymentOrigin != null) {
            super.setCgvUrl(paymentOrigin.getCgv());
        }
        super.setVatInformation(vatInformation);
    }

    private String generateInvoiceNumber(Long invoiceId) {
        return String.format("FP-%09d", invoiceId);
    }
}
