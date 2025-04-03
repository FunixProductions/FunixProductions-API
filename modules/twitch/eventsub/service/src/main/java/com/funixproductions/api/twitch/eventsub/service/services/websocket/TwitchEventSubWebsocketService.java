package com.funixproductions.api.twitch.eventsub.service.services.websocket;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.auth.client.enums.TwitchClientTokenType;
import com.funixproductions.api.twitch.eventsub.client.dtos.websocket.TwitchEventSubWebsocketMessage;
import com.funixproductions.core.tools.websocket.services.ApiWebsocketServerHandler;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Service who handles websocket data sending
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TwitchEventSubWebsocketService extends ApiWebsocketServerHandler {
    public static final String LISTEN_CALL_CLIENT = "listen";

    private final TwitchInternalAuthClient twitchInternalAuthClient;

    private final Cache<String, String> streamerIdCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    private final Map<String, List<String>> sessionsMapsStreamersEvents = new HashMap<>();
    private final Gson gson = new Gson();

    public void newNotification(final String notificationType, final String streamerId, final String data) {
        final String message = gson.toJson(new TwitchEventSubWebsocketMessage(
                streamerId,
                notificationType,
                data
        ));
        String sessionId;

        for (final Map.Entry<String, List<String>> entry : this.sessionsMapsStreamersEvents.entrySet()) {
            if (entry.getValue().contains(streamerId)) {
                sessionId = entry.getKey();

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
        final String[] data = message.split(":");

        if (data.length == 3 && data[0].equals(LISTEN_CALL_CLIENT)) {
            final String streamerId = this.getStreamerIdByUsername(
                    data[1],
                    TwitchClientTokenType.getTokenTypeByString(data[2])
            );
            if (streamerId == null) return;

            final List<String> streamersEvents = this.sessionsMapsStreamersEvents.getOrDefault(session.getId(), new ArrayList<>());

            streamersEvents.add(streamerId);
            this.sessionsMapsStreamersEvents.put(session.getId(), streamersEvents);
        }
    }

    @Override
    protected void onClientDisconnect(String sessionId) {
        this.sessionsMapsStreamersEvents.remove(sessionId);
    }

    @Nullable
    private String getStreamerIdByUsername(final String username, final TwitchClientTokenType tokenType) {
        String streamerId = this.streamerIdCache.getIfPresent(username);

        if (streamerId != null) {
            return streamerId;
        } else {
            try {
                streamerId = twitchInternalAuthClient.fetchTokenByStreamerName(username, tokenType.name()).getTwitchUserId();
                this.streamerIdCache.put(username, streamerId);

                return streamerId;
            } catch (Exception e) {
                log.error("Impossible de récupérer le streamer id par le username.", e);
                return null;
            }
        }
    }

}
