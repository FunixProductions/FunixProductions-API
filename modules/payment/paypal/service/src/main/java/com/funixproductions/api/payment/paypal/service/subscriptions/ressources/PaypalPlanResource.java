package com.funixproductions.api.payment.paypal.service.subscriptions.ressources;

import com.funixproductions.api.payment.paypal.client.clients.PaypalPlanClient;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalPlanDTO;
import com.funixproductions.api.payment.paypal.service.subscriptions.services.PaypalPlanService;
import com.funixproductions.core.crud.dtos.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paypal/plans")
@RequiredArgsConstructor
public class PaypalPlanResource implements PaypalPlanClient {

    private final PaypalPlanService paypalPlanService;

    @Override
    public PaypalPlanDTO create(PaypalPlanDTO paypalPlanDTO) {
        return this.paypalPlanService.create(paypalPlanDTO);
    }

    @Override
    public PaypalPlanDTO getPlanById(String id) {
        return this.paypalPlanService.getPlanById(id);
    }

    @Override
    public PageDTO<PaypalPlanDTO> getAll(String page, String elemsPerPage, String search, String sort) {
        return this.paypalPlanService.getAll(page, elemsPerPage, search, sort);
    }
}
