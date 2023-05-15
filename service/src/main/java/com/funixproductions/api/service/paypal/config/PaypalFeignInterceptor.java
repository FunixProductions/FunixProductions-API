package com.funixproductions.api.service.paypal.config;

import com.funixproductions.api.service.paypal.auth.services.PaypalAccessTokenService;
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
