package com.funixproductions.api.twitch.eventsub.client.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "TwitchEventSubInternalClient",
        url = "${funixproductions.api.twitch.eventsub.app-domain-url}",
        path = "/kubeinternal/twitch/eventsub/"
)
public interface TwitchEventSubInternalClient {

    /**
     * Documentation <a href="https://dev.twitch.tv/docs/api/reference/#create-eventsub-subscription">Create sub</a>
     * @param streamerUsername streamer username who has created streamer token with funix api
     */
    @PostMapping
    void createSubscription(@RequestBody String streamerUsername);

}
