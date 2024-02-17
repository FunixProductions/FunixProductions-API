package com.funixproductions.api.payment.paypal.service.orders.services;

import com.funixproductions.api.payment.billing.client.clients.BillingFeignInternalClient;
import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.client.dtos.BillingObjectDTO;
import com.funixproductions.api.payment.billing.client.enums.PaymentOrigin;
import com.funixproductions.api.payment.billing.client.enums.PaymentType;
import com.funixproductions.api.payment.paypal.client.dtos.requests.PaymentDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.PurchaseUnitDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.responses.PaypalOrderResponseDTO;
import com.funixproductions.api.payment.paypal.service.orders.entities.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceSenderService {

    private final BillingFeignInternalClient billingClient;

    public void sendInvoice(final PaypalOrderResponseDTO responseDTO, final OrderDTO orderDTO, @Nullable final PaymentDTO paymentDTO) {
        try {
            final BillingDTO billingDTO = new BillingDTO();

            billingDTO.setBillingDescription(this.generateInvoiceDescription(orderDTO));
            billingDTO.setPaymentType(this.getPaymentType(orderDTO));
            billingDTO.setBilledEntity(this.getBilledEntity(responseDTO, orderDTO));
            billingDTO.setPaymentOrigin(this.getPaymentOrigin(orderDTO));
            billingDTO.setAmountTotal(this.getAmountTotal(responseDTO));
            billingDTO.setVatInformation(orderDTO.getVatInformation());
            billingDTO.setBillingObjects(this.getBillingObjects(responseDTO, paymentDTO));

            billingClient.create(billingDTO);
        } catch (Exception e) {
            log.error("Erreur interne lors de l'envoi de la facture.", e);
        }
    }

    private String generateInvoiceDescription(final OrderDTO orderDTO) {
        return String.format("Facture générée suite à un achat via %s%s. Merci pour votre soutien.",
                orderDTO.getCardPayment() ? "carte bancaire" : "PayPal",
                orderDTO.getOriginRequest() == null ? "" : " sur " + orderDTO.getOriginRequest());
    }

    private PaymentType getPaymentType(final OrderDTO orderDTO) {
        if (orderDTO.getCardPayment()) {
            return PaymentType.CREDIT_CARD;
        } else {
            return PaymentType.PAYPAL;
        }
    }

    private BillingDTO.BilledEntity getBilledEntity(final PaypalOrderResponseDTO paypalOrderResponseDTO, final OrderDTO orderDTO) {
        return new BillingDTO.BilledEntity(
                orderDTO.getUsername(),
                null,
                null,
                null,
                null,
                orderDTO.getUserEmail(),
                null,
                null,
                null,
                orderDTO.getUserId()
        );
    }

    private PaymentOrigin getPaymentOrigin(final OrderDTO orderDTO) {
        if (orderDTO.getOriginRequest() == null) {
            return PaymentOrigin.OTHER;
        } else {
            final String origin = orderDTO.getOriginRequest().toLowerCase();

            if (origin.contains("pacifista")) {
                return PaymentOrigin.PACIFISTA;
            } else if (origin.contains("funixgaming")) {
                return PaymentOrigin.FUNIXGAMING;
            } else {
                return PaymentOrigin.OTHER;
            }
        }
    }

    private BillingDTO.Price getAmountTotal(final PaypalOrderResponseDTO paypalOrderResponseDTO) {
        final BillingDTO.Price price = new BillingDTO.Price(
                0.0,
                0.0,
                0.0,
                null
        );

        for (final PaypalOrderResponseDTO.PurchaseUnitResponse purchaseUnit : paypalOrderResponseDTO.getPurchaseUnits()) {
            final PurchaseUnitDTO.Amount.Breakdown breakdown = purchaseUnit.getAmount().getBreakdown();
            final double ht = Double.parseDouble(breakdown.getItemTotal().getValue());
            final double tax = Double.parseDouble(breakdown.getTaxTotal().getValue());

            price.setHt(price.getHt() + ht);
            price.setTax(price.getTax() + tax);
            price.setTtc(price.getTtc() + ht + tax);
        }
        return price;
    }

    private List<BillingObjectDTO> getBillingObjects(final PaypalOrderResponseDTO paypalOrderResponseDTO, @Nullable final PaymentDTO paymentDTO) {
        final List<BillingObjectDTO> toSend = new ArrayList<>();

        if (paymentDTO != null) {
            for (final PaymentDTO.PurchaseUnitDTO purchaseUnit : paymentDTO.getPurchaseUnits()) {
                for (PaymentDTO.PurchaseUnitDTO.Item item : purchaseUnit.getItems()) {
                    toSend.add(new BillingObjectDTO(
                            item.getName(),
                            item.getDescription(),
                            item.getQuantity(),
                            item.getPrice()
                    ));
                }
            }
        } else {
            for (final PaypalOrderResponseDTO.PurchaseUnitResponse purchaseUnit : paypalOrderResponseDTO.getPurchaseUnits()) {
                for (final PurchaseUnitDTO.Item item : purchaseUnit.getItems()) {
                    final double ht = Double.parseDouble(item.getUnitAmount().getValue());

                    toSend.add(new BillingObjectDTO(
                            item.getName(),
                            item.getDescription(),
                            Integer.parseInt(item.getQuantity()),
                            ht
                    ));
                }
            }
        }
        return toSend;
    }
}
