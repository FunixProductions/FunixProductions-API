package com.funixproductions.api.twitch.core.configs;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TwitchReferenceRequestInterceptor implements RequestInterceptor {

    private final TwitchApiConfig apiConfig;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Client-Id", apiConfig.getAppClientId());
    }
}
