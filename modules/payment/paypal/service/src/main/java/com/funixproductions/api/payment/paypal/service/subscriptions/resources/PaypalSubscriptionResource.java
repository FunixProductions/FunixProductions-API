package com.funixproductions.api.payment.paypal.service.subscriptions.resources;

import com.funixproductions.api.payment.paypal.client.clients.PaypalSubscriptionClient;
import com.funixproductions.api.payment.paypal.client.dtos.requests.paypal.PaypalCreateSubscriptionDTO;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalSubscriptionDTO;
import com.funixproductions.api.payment.paypal.service.subscriptions.services.PaypalSubscriptionService;
import com.funixproductions.core.crud.dtos.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paypal/subscriptions")
@RequiredArgsConstructor
public class PaypalSubscriptionResource implements PaypalSubscriptionClient {

    private final PaypalSubscriptionService paypalSubscriptionService;

    @Override
    public PaypalSubscriptionDTO subscribe(PaypalCreateSubscriptionDTO request) {
        return this.paypalSubscriptionService.subscribe(request);
    }

    @Override
    public PaypalSubscriptionDTO getSubscriptionById(String id) {
        return this.paypalSubscriptionService.getSubscriptionById(id);
    }

    @Override
    public PaypalSubscriptionDTO pauseSubscription(String id) {
        return this.paypalSubscriptionService.pauseSubscription(id);
    }

    @Override
    public PaypalSubscriptionDTO activateSubscription(String id) {
        return this.paypalSubscriptionService.activateSubscription(id);
    }

    @Override
    public void cancelSubscription(String id) {
        this.paypalSubscriptionService.cancelSubscription(id);
    }

    @Override
    public PageDTO<PaypalSubscriptionDTO> getAll(String page, String elemsPerPage, String search, String sort) {
        return this.paypalSubscriptionService.getAll(page, elemsPerPage, search, sort);
    }
}
