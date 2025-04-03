package com.funixproductions.api.payment.paypal.service.webhooks.services;

import com.google.gson.JsonObject;

public interface PaypalWebhookService {
    void handleWebhookEvent(JsonObject resource);
    String getEventType();
}
