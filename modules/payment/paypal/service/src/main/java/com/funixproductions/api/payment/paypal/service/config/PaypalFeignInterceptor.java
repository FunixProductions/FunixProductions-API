package com.funixproductions.api.payment.paypal.service.config;

import com.funixproductions.api.payment.paypal.service.auth.services.PaypalAccessTokenService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

@RequiredArgsConstructor
public class PaypalFeignInterceptor implements RequestInterceptor {

    private final PaypalAccessTokenService service;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + service.getAccessToken());
    }
}
