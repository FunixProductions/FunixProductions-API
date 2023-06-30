package com.funixproductions.api.twitch.eventsub.client.dtos.websocket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchEventSubWebsocketMessage {

    private String streamerId;

    private String notificationType;

    private String event;

}
