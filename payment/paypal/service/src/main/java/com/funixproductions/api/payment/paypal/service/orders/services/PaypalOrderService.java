package com.funixproductions.api.payment.paypal.service.orders.services;

import com.funixproductions.api.payment.paypal.service.orders.clients.PaypalFeignOrderClient;
import com.funixproductions.api.payment.paypal.service.orders.clients.PaypalOrderClient;
import com.funixproductions.api.payment.paypal.service.orders.dtos.requests.PaypalOrderCreationDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.responses.PaypalOrderResponseDTO;
import com.funixproductions.core.exceptions.ApiException;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaypalOrderService implements PaypalOrderClient {

    private final PaypalFeignOrderClient orderClient;

    @Override
    public PaypalOrderResponseDTO createOrder(@NonNull String metadataId,
                                              @NonNull String requestId,
                                              @NonNull PaypalOrderCreationDTO request) {
        try {
            return this.orderClient.createOrder(metadataId, requestId, request);
        } catch (FeignException e) {
            throw handleFeignException(e, "de créer");
        }
    }

    @Override
    public PaypalOrderResponseDTO getOrder(@NonNull String orderId) {
        try {
            return this.orderClient.getOrder(orderId);
        } catch (FeignException e) {
            throw handleFeignException(e, "de récupérer");
        }
    }

    @Override
    public PaypalOrderResponseDTO authorizeOrder(@NonNull String paypalAuthSession,
                                                 @NonNull String paypalClientMetadataId,
                                                 @NonNull String requestId,
                                                 @NonNull String orderId) {
        try {
            return this.orderClient.authorizeOrder(paypalAuthSession, paypalClientMetadataId, requestId, orderId);
        } catch (FeignException e) {
            throw handleFeignException(e, "d'autoriser");
        }
    }

    @Override
    public PaypalOrderResponseDTO captureOrder(@NonNull String paypalAuthSession,
                                               @NonNull String paypalClientMetadataId,
                                               @NonNull String requestId,
                                               @NonNull String orderId) {
        try {
            return this.orderClient.captureOrder(paypalAuthSession, paypalClientMetadataId, requestId, orderId);
        } catch (FeignException e) {
            throw handleFeignException(e, "de capturer");
        }
    }

    private ApiException handleFeignException(final FeignException e, final String action) {
        throw new ApiException(String.format("Impossible %s un ordre d'achat avec PayPal. Veuillez recommencer ou envoyer un mail à contact@funixgaming.fr", action), e);
    }
}
