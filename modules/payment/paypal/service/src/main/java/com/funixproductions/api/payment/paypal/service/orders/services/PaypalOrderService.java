package com.funixproductions.api.payment.paypal.service.orders.services;

import com.funixproductions.api.payment.paypal.service.orders.clients.PaypalFeignOrderClient;
import com.funixproductions.api.payment.paypal.service.orders.clients.PaypalOrderClient;
import com.funixproductions.api.payment.paypal.service.orders.dtos.requests.PaypalOrderCreationDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.responses.PaypalOrderErrorResponse;
import com.funixproductions.api.payment.paypal.service.orders.dtos.responses.PaypalOrderResponseDTO;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import com.google.gson.Gson;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaypalOrderService implements PaypalOrderClient {

    private final PaypalFeignOrderClient orderClient;
    private final Gson gson = new Gson();

    @Override
    public PaypalOrderResponseDTO createOrder(@NonNull String requestId,
                                              @NonNull PaypalOrderCreationDTO request) {
        try {
            return this.orderClient.createOrder(requestId, request);
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
    public PaypalOrderResponseDTO authorizeOrder(@NonNull String requestId,
                                                 @NonNull String orderId) {
        try {
            return this.orderClient.authorizeOrder(requestId, orderId);
        } catch (FeignException e) {
            throw handleFeignException(e, "d'autoriser");
        }
    }

    @Override
    public PaypalOrderResponseDTO captureOrder(@NonNull String requestId,
                                               @NonNull String orderId) {
        try {
            return this.orderClient.captureOrder(requestId, orderId);
        } catch (FeignException e) {
            throw handleFeignException(e, "de capturer");
        }
    }

    private ApiException handleFeignException(final FeignException e, final String action) {
        try {
            final PaypalOrderErrorResponse error = gson.fromJson(e.contentUTF8(), PaypalOrderErrorResponse.class);
            final StringBuilder message = new StringBuilder("Erreur avec paypal : ");

            if (error != null) {

                if (error.getName())
                message.append(error.getName().getFrMessage());

                if (error.getDetails() != null) {
                    message.append(" : ");
                    for (PaypalOrderErrorResponse.Details detail : error.getDetails()) {
                        if (detail.getIssue() == null) {
                            message.append(detail.getDescription()).append(" ");
                        } else {
                            message.append(detail.getIssue().getFrMessage()).append(" ");
                        }
                    }
                }
            } else {
                message.append("Impossible de récupérer le message d'erreur.");
            }

            final String errorMessage = message.toString();
            log.error("Impossible {} un ordre d'achat avec PayPal. Message Erreur {}. MessageBuilder: {}", action, e.getMessage(), errorMessage, e);

            final String finalMessage = String.format("%sEnvoyez un email à contact@funixproductions.com en cas de besoin.", errorMessage);
            if (error == null || error.getName() == null || !error.getName().equals(PaypalOrderErrorResponse.ErrorCode.UNPROCESSABLE_ENTITY)) {
                throw new ApiException(finalMessage, e);
            } else {
                return new ApiBadRequestException(finalMessage, e);
            }
        } catch (Exception ex) {
            log.error("ERREUR GESTION : Impossible {} un ordre d'achat avec PayPal. Message Erreur {}", action, e.getMessage(), e);
            throw new ApiException(String.format("Impossible %s un ordre d'achat avec PayPal. Veuillez recommencer ou envoyer un mail à contact@funixproductions.com", action), e);
    }
        }
}
