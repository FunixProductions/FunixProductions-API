package com.funixproductions.api.payment.paypal.service.webhooks.clients;

import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalSubscriptionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "PacifistaInternalPaymentCallbackClient",
        url = "http://web-shop-service",
        path = "/kubeinternal/web/shop/cb"
)
public interface PacifistaInternalPaymentCallbackClient {

    @PostMapping("/paypal-subscription")
    void sendPaymentCallback(@RequestBody PaypalSubscriptionDTO subscriptionDTO);

}
