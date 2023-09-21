package com.funixproductions.api.twitch.eventsub.service.services.websocket;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.eventsub.client.dtos.websocket.TwitchEventSubWebsocketMessage;
import com.funixproductions.core.tools.websocket.services.ApiWebsocketServerHandler;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Service who handles websocket data sending
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TwitchEventSubWebsocketService extends ApiWebsocketServerHandler {
    public static final String LISTEN_CALL_CLIENT = "listen";
    private final TwitchInternalAuthClient twitchInternalAuthClient;
    private final Map<String, String> sessionsMapsStreamersEvents = new HashMap<>();
    private final Gson gson = new Gson();

    public void newNotification(final String notificationType, final String streamerId, final String data) {
        final TwitchEventSubWebsocketMessage eventSubWebsocketMessage = new TwitchEventSubWebsocketMessage();
        eventSubWebsocketMessage.setStreamerId(streamerId);
        eventSubWebsocketMessage.setNotificationType(notificationType);
        eventSubWebsocketMessage.setEvent(data);
        final String message = gson.toJson(eventSubWebsocketMessage);

        for (final Map.Entry<String, String> entry : this.sessionsMapsStreamersEvents.entrySet()) {
            final String streamerIdEntry = entry.getValue();

            if (streamerIdEntry.equals(streamerId)) {
                final String sessionId = entry.getKey();

                try {
                    super.sendMessageToSessionId(sessionId, message);
                } catch (Exception e) {
                    log.error("Impossible d'envoyer un message websocket à la session id {}.", sessionId, e);
                }
            }
        }
    }

    @Override
    protected void newWebsocketMessage(@NonNull WebSocketSession session, @NonNull String message) {
        if (message.startsWith(LISTEN_CALL_CLIENT)) {
            final String[] data = message.split(":");

            if (data.length == 2) {
                final String streamerUsername = data[1];
                final TwitchClientTokenDTO token = twitchInternalAuthClient.fetchTokenByStreamerName(streamerUsername);

                this.sessionsMapsStreamersEvents.put(session.getId(), token.getTwitchUserId());
            }
        }
    }

    @Override
    protected void onClientDisconnect(String sessionId) {
        this.sessionsMapsStreamersEvents.remove(sessionId);
    }

}
