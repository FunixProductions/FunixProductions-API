package com.funixproductions.api.payment.paypal.service.subscriptions.services;

import com.funixproductions.api.payment.paypal.client.clients.PaypalSubscriptionClient;
import com.funixproductions.api.payment.paypal.client.dtos.requests.paypal.PaypalCreateSubscriptionDTO;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalSubscriptionDTO;
import com.funixproductions.core.crud.dtos.PageDTO;
import org.springframework.stereotype.Service;

@Service
public class PaypalSubscriptionService implements PaypalSubscriptionClient {

    @Override
    public PaypalSubscriptionDTO subscribe(PaypalCreateSubscriptionDTO request) {
        return null;
    }

    @Override
    public PaypalSubscriptionDTO getSubscriptionById(String id) {
        return null;
    }

    @Override
    public PaypalSubscriptionDTO pauseSubscription(String id) {
        return null;
    }

    @Override
    public PaypalSubscriptionDTO activateSubscription(String id) {
        return null;
    }

    @Override
    public void cancelSubscription(String id) {

    }

    @Override
    public PageDTO<PaypalSubscriptionDTO> getAll(String page, String elemsPerPage, String search, String sort) {
        return null;
    }
}
