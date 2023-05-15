package com.funixproductions.api.service.twitch.eventsub.services.handler;

import com.funixproductions.api.client.twitch.eventsub.dtos.events.channel.TwitchEventChannelFollowDTO;
import com.funixproductions.api.client.twitch.eventsub.dtos.events.channel.TwitchEventChannelUpdateDTO;
import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;
import com.funixproductions.api.service.twitch.eventsub.services.websocket.TwitchEventSubWebsocketService;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

/**
 * Service used to handle new notifications from callback service like a new follower or sub
 */
@Service
@RequiredArgsConstructor
public class TwitchEventSubHandlerService {

    private final TwitchEventSubWebsocketService websocketService;
    private final Gson gson = new Gson();

    public void receiveNewNotification(final String notificationType, final JsonObject event) {
        final String streamerId = getStreamerIdInNotification(event);
        if (streamerId == null) {
            return;
        }

        websocketService.newNotification(notificationType, streamerId, event.toString());

        if (notificationType.startsWith("channel") && !Strings.isNullOrEmpty(streamerId)) {
            handleChannelNotification(notificationType, streamerId, event);
        }
    }

    private void handleChannelNotification(final String notificationType, final String streamerId, final JsonObject event) {
        if (notificationType.equals(ChannelEventType.FOLLOW.getType())) {
            final TwitchEventChannelFollowDTO followDTO = gson.fromJson(event, TwitchEventChannelFollowDTO.class);

        } else if (notificationType.equals(ChannelEventType.UPDATE.getType())) {
            final TwitchEventChannelUpdateDTO updateDTO = gson.fromJson(event, TwitchEventChannelUpdateDTO.class);
        }
    }

    @Nullable
    private String getStreamerIdInNotification(final JsonObject jsonObject) {
        final JsonElement idJson = jsonObject.get("broadcaster_user_id");

        if (idJson != null && idJson.isJsonPrimitive()) {
            final JsonPrimitive id = idJson.getAsJsonPrimitive();
            return id.getAsString();
        } else {
            return null;
        }
    }

}
