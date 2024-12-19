package com.funixproductions.api.twitch.eventsub.service.resources;

import com.funixproductions.api.twitch.eventsub.client.clients.TwitchEventSubClient;
import com.funixproductions.api.twitch.eventsub.client.dtos.TwitchEventSubListDTO;
import com.funixproductions.api.twitch.eventsub.service.services.TwitchEventSubCallbackService;
import com.funixproductions.api.twitch.eventsub.service.services.TwitchEventSubReferenceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/eventsub")
@RequiredArgsConstructor
public class TwitchEventSubResource implements TwitchEventSubClient {

    private final TwitchEventSubCallbackService twitchEventSubCallbackService;
    private final TwitchEventSubReferenceService twitchEventSubReferenceService;

    @Override
    public TwitchEventSubListDTO getSubscriptions(String status, String type, String userId, String after) {
        return twitchEventSubReferenceService.getSubscriptions(status, type, userId, after);
    }

    @PostMapping("cb")
    public String handleTwitchCallback(@RequestBody final byte[] body,
                                       final HttpServletRequest servletRequest) {
        return twitchEventSubCallbackService.handleNewWebhook(servletRequest, body);
    }

}
