package com.funixproductions.api.payment.paypal.service.webhooks.services;

import com.funixproductions.api.payment.billing.client.clients.BillingFeignInternalClient;
import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.client.dtos.BillingObjectDTO;
import com.funixproductions.api.payment.billing.client.enums.PaymentOrigin;
import com.funixproductions.api.payment.billing.client.enums.PaymentType;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalSubscriptionDTO;
import com.funixproductions.api.payment.paypal.service.subscriptions.services.PaypalSubscriptionService;
import com.funixproductions.api.payment.paypal.service.webhooks.clients.PacifistaInternalPaymentCallbackClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SaleCompleteWebhookService")
public class SubscriptionsWebhookPaypalService implements PaypalWebhookService {

    private static final String WEBHOOK_ID = "PAYMENT.SALE.COMPLETED";

    private final BillingFeignInternalClient billingClient;
    private final PaypalSubscriptionService subscriptionService;
    private final PacifistaInternalPaymentCallbackClient pacifistaInternalPaymentCallbackClient;

    @Override
    public void handleWebhookEvent(JsonObject resource) {
        final JsonElement jsonSubscriptionId = resource.get("billing_agreement_id");
        if (jsonSubscriptionId.isJsonNull()) return;

        final PaypalSubscriptionDTO subscription = this.subscriptionService.getSubscriptionByPaypalId(jsonSubscriptionId.getAsString());
        if (subscription == null) return;

        this.sendBillingInvoice(subscription);

        try {
            final String projectName = subscription.getPlan().getProjectName().toLowerCase();

            if (projectName.contains("pacifista")) {
                this.pacifistaInternalPaymentCallbackClient.sendPaymentCallback(subscription);
            }
        } catch (Exception e) {
            throw new ApiException("Le callback vers le service Pacifista web a échoué.", e);
        }
    }

    @Override
    public String getEventType() {
        return WEBHOOK_ID;
    }

    private void sendBillingInvoice(final PaypalSubscriptionDTO subscription) {
        final UserDTO user = this.subscriptionService.getCurrentUser(subscription.getFunixProdUserId());
        final VATInformation vatInformation = VATInformation.getVATInformation(user.getCountry().getCountryCode2Chars());
        final BillingDTO billingDTO = new BillingDTO();

        billingDTO.setBillingDescription(String.format(
                "Facture générée suite à un achat de l'abonnement %s. Merci pour votre soutien. Paiement via PayPal.",
                subscription.getPlan().getName()
        ));
        billingDTO.setPaymentType(PaymentType.PAYPAL);
        billingDTO.setBilledEntity(this.createBilledDTO(user));
        billingDTO.setPaymentOrigin(this.getPaymentOrigin(subscription));
        billingDTO.setAmountTotal(new BillingDTO.Price(
                subscription.getPlan().getPrice(),
                subscription.getPlan().getPrice() * (vatInformation == null ? 0.0 : vatInformation.getVatRate() / 100)
        ));
        billingDTO.setVatInformation(vatInformation);
        billingDTO.setBillingObjects(List.of(
                new BillingObjectDTO(
                        subscription.getPlan().getName(),
                        subscription.getPlan().getDescription(),
                        1,
                        subscription.getPlan().getPrice()
                )
        ));

        try {
            this.billingClient.create(billingDTO);
        } catch (Exception e) {
            throw new ApiException(String.format("Erreur interne lors de l'envoi de la facture pour le paypalsub id: %s, subName: %s.", subscription.getId(), subscription.getPlan().getName()), e);
        }
    }

    private BillingDTO.BilledEntity createBilledDTO(final UserDTO user) {
        return new BillingDTO.BilledEntity(
                user.getUsername(),
                null,
                null,
                null,
                null,
                user.getEmail(),
                null,
                null,
                null,
                user.getId().toString()
        );
    }

    private PaymentOrigin getPaymentOrigin(final PaypalSubscriptionDTO subscription) {
        final String project = subscription.getPlan().getProjectName().toLowerCase();

        if (project.contains("pacifista")) {
            return PaymentOrigin.PACIFISTA;
        } else if (project.contains("funixgaming")) {
            return PaymentOrigin.FUNIXGAMING;
        } else {
            return PaymentOrigin.OTHER;
        }
    }

}
