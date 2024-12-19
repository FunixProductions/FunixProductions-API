package com.funixproductions.api.twitch.eventsub.service.resources;

import com.funixproductions.api.twitch.eventsub.client.clients.TwitchEventSubInternalClient;
import com.funixproductions.api.twitch.eventsub.service.services.TwitchEventSubRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kubeinternal/twitch/eventsub")
@RequiredArgsConstructor
public class TwitchEventSubInternalResource implements TwitchEventSubInternalClient {

    private final TwitchEventSubRegistrationService twitchEventSubRegistrationService;

    @Override
    public void createSubscription(String streamerUsername) {
        twitchEventSubRegistrationService.createSubscription(streamerUsername);
    }

    @Override
    public void deleteSubscription(String streamerUsername) {
        twitchEventSubRegistrationService.removeSubscription(streamerUsername);
    }

}
