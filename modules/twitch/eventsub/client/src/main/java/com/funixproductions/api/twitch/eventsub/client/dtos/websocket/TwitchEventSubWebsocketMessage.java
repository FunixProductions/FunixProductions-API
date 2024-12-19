package com.funixproductions.api.twitch.eventsub.client.dtos.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TwitchEventSubWebsocketMessage {

    private String streamerId;

    private String notificationType;

    private String event;

}
