package com.funixproductions.api.service.core.configs;

import com.google.common.net.HttpHeaders;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignConfiguration implements RequestInterceptor {

    private final String accessToken = System.getenv("FUNIXPROD_API_AUTH_TOKEN");

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    }

}
