package com.funixproductions.api.payment.paypal.service.subscriptions.services;

import com.funixproductions.api.payment.paypal.client.clients.PaypalPlanClient;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalPlanDTO;
import com.funixproductions.core.crud.dtos.PageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "PaypalPlanService")
public class PaypalPlanService implements PaypalPlanClient {

    @Override
    public PaypalPlanDTO create(PaypalPlanDTO paypalPlanDTO) {
        return null;
    }

    @Override
    public PaypalPlanDTO getPlanById(String id) {
        return null;
    }

    @Override
    public PaypalPlanDTO updatePlan(String id, PaypalPlanDTO paypalPlanDTO) {
        return null;
    }

    @Override
    public PaypalPlanDTO activatePlan(String id) {
        return null;
    }

    @Override
    public PaypalPlanDTO deactivatePlan(String id) {
        return null;
    }

    @Override
    public PaypalPlanDTO updatePricePlan(String id, Double newPrice) {
        return null;
    }

    @Override
    public PageDTO<PaypalPlanDTO> getAll(String page, String elemsPerPage, String search, String sort) {
        return null;
    }
}
