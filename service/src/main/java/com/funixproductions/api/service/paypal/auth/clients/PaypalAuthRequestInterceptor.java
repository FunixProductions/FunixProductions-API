package com.funixproductions.api.service.paypal.auth.clients;

import com.funixproductions.api.service.paypal.config.PaypalConfig;
import feign.auth.BasicAuthRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class PaypalAuthRequestInterceptor {

    private final PaypalConfig paypalConfig;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(paypalConfig.getClientId(), paypalConfig.getClientSecret());
    }
}
