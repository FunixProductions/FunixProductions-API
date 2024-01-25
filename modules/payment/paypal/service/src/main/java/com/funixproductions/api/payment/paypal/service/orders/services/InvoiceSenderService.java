package com.funixproductions.api.payment.paypal.service.orders.services;

import com.funixproductions.api.payment.billing.client.clients.BillingFeignInternalClient;
import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.client.dtos.BillingObjectDTO;
import com.funixproductions.api.payment.billing.client.enums.PaymentOrigin;
import com.funixproductions.api.payment.billing.client.enums.PaymentType;
import com.funixproductions.api.payment.paypal.client.dtos.requests.PaymentDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.responses.PaypalOrderResponseDTO;
import com.funixproductions.api.payment.paypal.service.orders.entities.OrderDTO;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceSenderService {

    private final BillingFeignInternalClient billingClient;

    public void sendInvoice(final PaypalOrderResponseDTO responseDTO, final OrderDTO orderDTO, @Nullable final PaymentDTO paymentDTO) {
        try {
            final BillingDTO billingDTO = new BillingDTO();

            billingDTO.setBillingDescription(this.generateInvoiceDescription(responseDTO));
            billingDTO.setPaymentType(this.getPaymentType(orderDTO));
            billingDTO.setBilledEntity(this.getBilledEntity(responseDTO, orderDTO));
            billingDTO.setPaymentOrigin(this.getPaymentOrigin(orderDTO));
            billingDTO.setAmountTotal(this.getAmountTotal(responseDTO));
            billingDTO.setVatInformation(this.getVatInformation(orderDTO));
            billingDTO.setBillingObjects(this.getBillingObjects(responseDTO, paymentDTO));

            billingClient.create(billingDTO);
        } catch (Exception e) {
            log.error("Erreur interne lors de l'envoi de la facture.", e);
        }
    }

    private String generateInvoiceDescription(final PaypalOrderResponseDTO paypalOrderResponseDTO) {
    }

    private PaymentType getPaymentType(final OrderDTO orderDTO) {
        if (orderDTO.getCardPayment()) {
            return PaymentType.CREDIT_CARD;
        } else {
            return PaymentType.PAYPAL;
        }
    }

    private BillingDTO.BilledEntity getBilledEntity(final PaypalOrderResponseDTO paypalOrderResponseDTO, final OrderDTO orderDTO) {
    }

    private PaymentOrigin getPaymentOrigin(final OrderDTO orderDTO) {

    }

    private BillingDTO.Price getAmountTotal(final PaypalOrderResponseDTO paypalOrderResponseDTO) {

    }

    private VATInformation getVatInformation(final OrderDTO paypalOrderResponseDTO) {

    }

    private List<BillingObjectDTO> getBillingObjects(final PaypalOrderResponseDTO paypalOrderResponseDTO, @Nullable final PaymentDTO paymentDTO) {

    }
}
