package com.funixproductions.api.payment.paypal.client.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "FunixProductionsPaypalOrderClient",
        url = "http://payment-paypal",
        path = "/paypal/orders"
)
public interface PaypalOrderFeignClient extends PaypalOrderClient {
}
