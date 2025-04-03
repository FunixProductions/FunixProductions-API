package com.funixproductions.api.payment.paypal.service.subscriptions.services;

import com.funixproductions.api.payment.paypal.client.clients.PaypalPlanClient;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalPlanDTO;
import com.funixproductions.api.payment.paypal.service.subscriptions.clients.PaypalServicePlansClient;
import com.funixproductions.api.payment.paypal.service.subscriptions.clients.PaypalServiceProductsClient;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.requests.CreatePaypalPlanRequest;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.requests.CreatePaypalProductRequest;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses.PaypalPlanResponse;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses.PaypalProductResponse;
import com.funixproductions.core.crud.dtos.PageDTO;
import com.funixproductions.core.exceptions.ApiException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "PaypalPlanService")
@RequiredArgsConstructor
public class PaypalPlanService implements PaypalPlanClient {

    private final PaypalServiceProductsClient paypalServiceProductsClient;
    private final PaypalServicePlansClient paypalServicePlansClient;
    private final PaypalPlanCrudService paypalPlanCrudService;

    @Override
    @Transactional
    public PaypalPlanDTO create(PaypalPlanDTO paypalPlanDTO) {
        final PaypalPlanDTO createdPlan = this.paypalPlanCrudService.create(paypalPlanDTO);
        final PaypalProductResponse productResponse = this.createProduct(createdPlan);
        final PaypalPlanResponse planResponse = this.createPlan(createdPlan, productResponse);

        log.info("Created plan -> paypal-id: {} plan-name: {}", planResponse, paypalPlanDTO.getName());
        createdPlan.setPlanId(planResponse.getId());
        return this.paypalPlanCrudService.update(createdPlan);
    }

    @Override
    public PaypalPlanDTO getPlanById(String id) {
        return this.paypalPlanCrudService.findById(id);
    }

    @Override
    public PageDTO<PaypalPlanDTO> getAll(String page, String elemsPerPage, String search, String sort) {
        return this.paypalPlanCrudService.getAll(
                page,
                elemsPerPage,
                search,
                sort
        );
    }

    @NonNull
    private PaypalProductResponse createProduct(final PaypalPlanDTO paypalPlanDTO) throws ApiException {
        try {
            if (paypalPlanDTO.getId() == null) {
                throw new ApiException("L'id du plan Paypal est null. Vous devez d'abord créer le dto en bdd.");
            }

            return this.paypalServiceProductsClient.createProduct(
                    paypalPlanDTO.getId().toString(),
                    new CreatePaypalProductRequest(paypalPlanDTO)
            );
        } catch (Exception e) {
            throw new ApiException("Erreur lors de la création du produit sur Paypal.", e);
        }
    }

    @NonNull
    private PaypalPlanResponse createPlan(final PaypalPlanDTO paypalPlanDTO, final PaypalProductResponse productResponse) throws ApiException {
        try {
            if (paypalPlanDTO.getId() == null) {
                throw new ApiException("L'id du plan Paypal est null. Vous devez d'abord créer le dto en bdd.");
            }

            return this.paypalServicePlansClient.createPlan(
                    paypalPlanDTO.getId().toString(),
                    new CreatePaypalPlanRequest(paypalPlanDTO, productResponse)
            );
        } catch (Exception e) {
            throw new ApiException("Erreur lors de la création du plan sur Paypal.", e);
        }
    }
}
