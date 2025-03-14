package com.funixproductions.api.payment.paypal.service.subscriptions.clients;

import com.funixproductions.api.payment.paypal.service.config.PaypalFeignInterceptor;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.requests.CreatePaypalPlanRequest;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses.PaypalPlanResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * <a href="https://developer.paypal.com/docs/api/subscriptions/v1/#plans_create">Plans</a>
 */
@FeignClient(
        name = "PaypalServicePlansClient",
        url = "${paypal.paypal-domain}",
        path = "/v1/billing/plans/",
        configuration = PaypalFeignInterceptor.class
)
public interface PaypalServicePlansClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    PaypalPlanResponse createPlan(
            @RequestHeader(name = "PayPal-Request-Id") String requestId,
            @RequestBody CreatePaypalPlanRequest request
    );

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    PaypalPlanResponse getPlan(@PathVariable String id);

}
