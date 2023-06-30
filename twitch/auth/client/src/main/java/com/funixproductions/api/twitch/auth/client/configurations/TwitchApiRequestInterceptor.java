package com.funixproductions.api.twitch.auth.client.configurations;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TwitchApiRequestInterceptor implements RequestInterceptor {

    private final TwitchApiConfig apiConfig;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Client-Id", apiConfig.getAppClientId());
    }
}
