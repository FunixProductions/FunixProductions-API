package com.funixproductions.api.twitch.eventsub.service.services;

import com.funixproductions.api.twitch.eventsub.service.services.websocket.TwitchEventSubWebsocketService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Service used for handling the public callback route for twitch
 */
@Service
@RequiredArgsConstructor
public class TwitchEventSubCallbackService {

    public static final String TWITCH_MESSAGE_ID = "Twitch-Eventsub-Message-Id";
    public static final String TWITCH_MESSAGE_TYPE = "Twitch-Eventsub-Message-Type";

    public static final String MESSAGE_TYPE_NOTIFICATION = "notification";
    public static final String MESSAGE_TYPE_VERIFICATION = "webhook_callback_verification";
    public static final String MESSAGE_TYPE_REVOCATION = "revocation";

    private static final String BODY_JSON_NOT_VALID = "Le body ne vient pas de twitch (malformé).";

    private final TwitchEventSubHmacService hmacService;
    private final TwitchEventSubWebsocketService websocketService;

    private final Cache<String, Boolean> messagesIdsTreated = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    /**
     * Method called every time Twitch sends a call
     * @param httpServletRequest servlet request generated by spring
     * @param body the body of the twitch message
     * @throws ApiBadRequestException error
     */
    public String handleNewWebhook(final HttpServletRequest httpServletRequest, final byte[] body) throws ApiBadRequestException {
        final String messageId = httpServletRequest.getHeader(TWITCH_MESSAGE_ID);
        final String messageType = httpServletRequest.getHeader(TWITCH_MESSAGE_TYPE);
        if (messageId == null) {
            throw new ApiBadRequestException("Il manque le message id");
        }
        if (messageType == null) {
            throw new ApiBadRequestException("Il manque le message type");
        }

        if (Boolean.TRUE.equals(this.messagesIdsTreated.getIfPresent(messageId))) {
            throw new ApiBadRequestException("Cette notification twitch a déjà été traitée.");
        } else {
            this.hmacService.validEventMessage(httpServletRequest, body);
            this.messagesIdsTreated.put(messageId, true);

            final String bodyParsed = new String(body, StandardCharsets.UTF_8);

            switch (messageType) {
                case MESSAGE_TYPE_NOTIFICATION -> {
                    final JsonObject message = this.getTwitchMessage(bodyParsed);
                    final JsonElement eventElement = message.get("event");

                    if (eventElement.isJsonObject()) {
                        final String notificationType = this.getNotificationType(message);
                        final JsonObject event = eventElement.getAsJsonObject();
                        final String streamerId = this.getStreamerIdInNotification(event);

                        if (!Strings.isNullOrEmpty(streamerId)) {
                            this.websocketService.newNotification(
                                    notificationType,
                                    streamerId,
                                    event.toString()
                            );
                        }
                    } else {
                        throw new ApiBadRequestException(BODY_JSON_NOT_VALID);
                    }

                    return "s";
                }
                case MESSAGE_TYPE_VERIFICATION -> {
                    return this.getChallenge(
                            this.getTwitchMessage(bodyParsed)
                    );
                }
                case MESSAGE_TYPE_REVOCATION -> {
                    return "s";
                }
                default -> throw new ApiBadRequestException("Le message type n'existe pas.");
            }
        }
    }

    private String getNotificationType(final JsonObject message) {
        final JsonElement subElement = message.get("subscription");

        if (subElement.isJsonObject()) {
            final JsonObject subscription = subElement.getAsJsonObject();
            final JsonElement subType = subscription.get("type");

            if (subType.isJsonPrimitive()) {
                final JsonPrimitive subTypeStr = subType.getAsJsonPrimitive();
                return subTypeStr.getAsString();
            } else {
                throw new ApiBadRequestException(BODY_JSON_NOT_VALID);
            }
        } else {
            throw new ApiBadRequestException(BODY_JSON_NOT_VALID);
        }
    }

    private String getChallenge(final JsonObject message) {
        final JsonElement challengeElement = message.get("challenge");

        if (challengeElement.isJsonPrimitive()) {
            final JsonPrimitive challenge = challengeElement.getAsJsonPrimitive();
            return challenge.getAsString();
        } else {
            throw new ApiBadRequestException(BODY_JSON_NOT_VALID);
        }
    }

    private JsonObject getTwitchMessage(final String object) {
        final JsonElement element = JsonParser.parseString(object);

        if (element.isJsonObject()) {
            return element.getAsJsonObject();
        } else {
            throw new ApiBadRequestException(BODY_JSON_NOT_VALID);
        }
    }

    @Nullable
    private String getStreamerIdInNotification(final JsonObject jsonObject) {
        final JsonElement idJson = jsonObject.get("broadcaster_user_id");

        if (idJson != null && idJson.isJsonPrimitive()) {
            return idJson.getAsJsonPrimitive().getAsString();
        } else {
            return null;
        }
    }

}
