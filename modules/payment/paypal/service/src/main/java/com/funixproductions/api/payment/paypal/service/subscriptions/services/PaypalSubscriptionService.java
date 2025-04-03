package com.funixproductions.api.payment.paypal.service.subscriptions.services;

import com.funixproductions.api.payment.paypal.client.clients.PaypalSubscriptionClient;
import com.funixproductions.api.payment.paypal.client.dtos.requests.paypal.PaypalCreateSubscriptionDTO;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalSubscriptionDTO;
import com.funixproductions.api.payment.paypal.service.subscriptions.clients.PaypalServiceSubscriptionsClient;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.requests.CreatePaypalSubscriptionRequest;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses.PaypalSubscriptionResponse;
import com.funixproductions.api.user.client.clients.InternalUserCrudClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.core.crud.dtos.PageDTO;
import com.funixproductions.core.crud.enums.SearchOperation;
import com.funixproductions.core.exceptions.ApiException;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaypalSubscriptionService implements PaypalSubscriptionClient {

    private static final String PAUSE_SUBSCRIPTION_REASON = "La pause de l'abonnement a été demandée par l'utilisateur.";
    private static final String CANCEL_SUBSCRIPTION_REASON = "L'annulation de l'abonnement a été demandée par l'utilisateur.";

    private final PaypalServiceSubscriptionsClient paypalServiceSubscriptionsClient;
    private final PaypalSubscriptionCrudService subscriptionCrudService;
    private final InternalUserCrudClient internalUserCrudClient;

    @Override
    @Transactional
    public PaypalSubscriptionDTO subscribe(PaypalCreateSubscriptionDTO request) {
        final PaypalSubscriptionDTO subscriptionDTO = this.subscriptionCrudService.createFromRequest(request);
        final PaypalSubscriptionResponse subscriptionResponse = this.createSubscriptionFromPaypalAPI(request, subscriptionDTO);

        mapDtoWithPaypalEntity(subscriptionDTO, subscriptionResponse);
        final PaypalSubscriptionDTO res = this.subscriptionCrudService.updatePut(subscriptionDTO);

        res.setApproveLink(subscriptionDTO.getApproveLink());
        return res;
    }

    @Override
    @Transactional
    public PaypalSubscriptionDTO getSubscriptionById(String id) {
        final PaypalSubscriptionDTO subscriptionDTO = this.subscriptionCrudService.findById(id);
        final PaypalSubscriptionResponse subscriptionResponse = this.paypalServiceSubscriptionsClient.getSubscription(subscriptionDTO.getSubscriptionId());

        mapDtoWithPaypalEntity(subscriptionDTO, subscriptionResponse);
        final PaypalSubscriptionDTO res = this.subscriptionCrudService.updatePut(subscriptionDTO);

        res.setApproveLink(subscriptionDTO.getApproveLink());
        return res;
    }

    @Override
    @Transactional
    public PaypalSubscriptionDTO pauseSubscription(String id) {
        final PaypalSubscriptionDTO paypalSubscriptionDTO = this.getSubscriptionById(id);

        try {
            this.paypalServiceSubscriptionsClient.pauseSubscription(paypalSubscriptionDTO.getSubscriptionId(), PAUSE_SUBSCRIPTION_REASON);
            paypalSubscriptionDTO.setActive(false);

            final PaypalSubscriptionDTO res = this.subscriptionCrudService.updatePut(paypalSubscriptionDTO);
            res.setApproveLink(paypalSubscriptionDTO.getApproveLink());
            return res;
        } catch (Exception e) {
            throw new ApiException("Une erreur est survenue lors de la mise en pause de l'abonnement PayPal.", e);
        }
    }

    @Override
    @Transactional
    public PaypalSubscriptionDTO activateSubscription(String id) {
        final PaypalSubscriptionDTO paypalSubscriptionDTO = this.getSubscriptionById(id);

        try {
            this.paypalServiceSubscriptionsClient.activateSubscription(paypalSubscriptionDTO.getSubscriptionId());
            paypalSubscriptionDTO.setActive(true);

            final PaypalSubscriptionDTO res = this.subscriptionCrudService.updatePut(paypalSubscriptionDTO);
            res.setApproveLink(paypalSubscriptionDTO.getApproveLink());
            return res;
        } catch (Exception e) {
            throw new ApiException("Une erreur est survenue lors de l'activation de l'abonnement PayPal.", e);
        }
    }

    @Override
    @Transactional
    public void cancelSubscription(String id) {
        final PaypalSubscriptionDTO paypalSubscriptionDTO = this.getSubscriptionById(id);

        try {
            this.paypalServiceSubscriptionsClient.cancelSubscription(paypalSubscriptionDTO.getSubscriptionId(), CANCEL_SUBSCRIPTION_REASON);
            this.subscriptionCrudService.delete(paypalSubscriptionDTO.getId().toString());
        } catch (Exception e) {
            throw new ApiException("Une erreur est survenue lors de l'annulation de l'abonnement PayPal.", e);
        }
    }

    @Override
    public PageDTO<PaypalSubscriptionDTO> getAll(String page, String elemsPerPage, String search, String sort) {
        return this.subscriptionCrudService.getAll(page, elemsPerPage, search, sort);
    }

    @Nullable
    @Transactional
    public PaypalSubscriptionDTO getSubscriptionByPaypalId(String paypalId) {
        final PageDTO<PaypalSubscriptionDTO> page = this.subscriptionCrudService.getAll(
                "0",
                "1",
                String.format(
                        "subscriptionId:%s:%s",
                        SearchOperation.EQUALS.getOperation(),
                        paypalId
                ),
                ""
        );

        try {
            final PaypalSubscriptionDTO paypalSubscriptionDTO = page.getContent().getFirst();
            final PaypalSubscriptionResponse subscriptionResponse = this.paypalServiceSubscriptionsClient.getSubscription(paypalSubscriptionDTO.getSubscriptionId());

            mapDtoWithPaypalEntity(paypalSubscriptionDTO, subscriptionResponse);
            return this.subscriptionCrudService.updatePut(paypalSubscriptionDTO);
        } catch (NoSuchElementException e) {
            return null;
        } catch (Exception e) {
            throw new ApiException("Une erreur est survenue lors de la récupération de l'abonnement PayPal.", e);
        }
    }

    @NonNull
    private PaypalSubscriptionResponse createSubscriptionFromPaypalAPI(PaypalCreateSubscriptionDTO request, PaypalSubscriptionDTO dto) throws ApiException {
        if (dto.getId() == null) {
            throw new ApiException("L'identifiant unique de l'abonnement est obligatoire.");
        }

        final CreatePaypalSubscriptionRequest paypalSubscriptionRequest = new CreatePaypalSubscriptionRequest(
                dto.getPlan().getPlanId(),
                this.getCurrentUser(request.getFunixProdUserId()),
                request.getBrandName(),
                request.getReturnUrl(),
                request.getCancelUrl()
        );

        try {
            return this.paypalServiceSubscriptionsClient.createSubscription(
                    dto.getId().toString(),
                    paypalSubscriptionRequest
            );
        } catch (Exception e) {
            throw new ApiException("Une erreur est survenue lors de la création de l'abonnement PayPal.", e);
        }
    }

    @NonNull
    public UserDTO getCurrentUser(final UUID funixProdUserId) throws ApiException {
        try {
            return this.internalUserCrudClient.findById(funixProdUserId.toString());
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new ApiException("L'utilisateur id: " + funixProdUserId + " n'existe pas.");
            } else {
                throw new ApiException("Une erreur est survenue lors de la récupération de l'utilisateur id: " + funixProdUserId + ".", e);
            }
        } catch (Exception e) {
            throw new ApiException("Une erreur est survenue lors de la récupération de l'utilisateur id: " + funixProdUserId + ".", e);
        }
    }

    private static void mapDtoWithPaypalEntity(PaypalSubscriptionDTO subscriptionDTO, PaypalSubscriptionResponse subscriptionResponse) {
        subscriptionDTO.setSubscriptionId(subscriptionResponse.getId());
        subscriptionDTO.setCyclesCompleted(subscriptionResponse.getCyclesCompleted());
        subscriptionDTO.setNextPaymentDate(subscriptionResponse.getNextBillingDate());
        subscriptionDTO.setLastPaymentDate(subscriptionResponse.getLastPaymentDate());
        subscriptionDTO.setApproveLink(subscriptionResponse.getApproveLink());
        subscriptionDTO.setActive(subscriptionResponse.isActive() && !subscriptionResponse.isPaused());
    }
}
