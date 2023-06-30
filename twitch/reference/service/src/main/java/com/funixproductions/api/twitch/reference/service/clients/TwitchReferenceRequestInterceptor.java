package com.funixproductions.api.twitch.reference.service.clients;

import com.funixproductions.api.twitch.reference.service.configurations.TwitchReferenceConfig;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TwitchReferenceRequestInterceptor implements RequestInterceptor {

    private final TwitchReferenceConfig apiConfig;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Client-Id", apiConfig.getAppClientId());
    }
}
