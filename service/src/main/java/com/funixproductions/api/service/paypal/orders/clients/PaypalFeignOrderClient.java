package com.funixproductions.api.service.paypal.orders.clients;

import com.funixproductions.api.service.paypal.config.PaypalFeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <a href="https://developer.paypal.com/docs/api/orders/v2/">Main documentation</a>
 */
@FeignClient(
        name = "PaypalOrderClient",
        url = "${paypal.paypal-domain}",
        path = "/v2/checkout/orders/",
        configuration = PaypalFeignInterceptor.class
)
public interface PaypalFeignOrderClient extends PaypalOrderClient {
}
