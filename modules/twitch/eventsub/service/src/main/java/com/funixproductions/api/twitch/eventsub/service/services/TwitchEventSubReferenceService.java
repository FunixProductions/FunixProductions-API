package com.funixproductions.api.twitch.eventsub.service.services;

import com.funixproductions.api.twitch.auth.client.clients.TwitchInternalAuthClient;
import com.funixproductions.api.twitch.eventsub.client.dtos.TwitchEventSubListDTO;
import com.funixproductions.api.twitch.eventsub.service.clients.TwitchEventSubReferenceClient;
import com.funixproductions.api.twitch.eventsub.service.configs.TwitchEventSubConfig;
import com.funixproductions.api.twitch.eventsub.service.requests.TwitchSubscription;
import com.funixproductions.api.twitch.reference.client.services.TwitchReferenceService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

/**
 * Service who encapsulates the twitch client who calls the twitch api
 */
@Service
@Slf4j(topic = "TwitchEventSubReferenceService")
@RequiredArgsConstructor
public class TwitchEventSubReferenceService extends TwitchReferenceService {

    private final TwitchEventSubHmacService hmacService;
    private final TwitchInternalAuthClient tokenService;
    private final TwitchEventSubReferenceClient eventSubReferenceClient;
    private final TwitchEventSubConfig twitchEventSubConfig;

    public TwitchEventSubListDTO getSubscriptions(@Nullable String status,
                                                  @Nullable String type,
                                                  @Nullable String userId,
                                                  @Nullable String after) {
        try {
            return this.eventSubReferenceClient.getSubscriptions(
                    super.addBearerPrefix(tokenService.fetchServerToken()),
                    status,
                    type,
                    userId,
                    after
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }

    /**
     * Create a subscription to twitch eventsub
     * @param request the subscription request
     * @throws ApiException when error
     * @throws ApiBadRequestException when conflicts
     */
    public void createSubscription(@NonNull final TwitchSubscription request) throws ApiException, ApiBadRequestException {
        request.setEventUrlCallback(twitchEventSubConfig.getDomainUrlAppCallback() + "/twitch/eventsub/cb");
        request.setSecretHmacKey(hmacService.getKey());

        try {
            this.eventSubReferenceClient.createSubscription(
                    super.addBearerPrefix(tokenService.fetchServerToken()),
                    MediaType.APPLICATION_JSON_VALUE,
                    request.getPayload()
            );
        } catch (FeignException e) {
            final int statusCode = e.status();

            if (statusCode == 400) {
                throw new ApiException("Erreur 400 Bad Request de Twitch lors de la création d'une subscription Twitch.", e);
            } else if (statusCode == 401) {
                throw new ApiException("Erreur 401 Unauthorized de Twitch lors de la création d'une subscription Twitch.", e);
            } else if (statusCode == 403) {
                throw new ApiException("Erreur 403 Forbidden de Twitch lors de la création d'une subscription Twitch.", e);
            } else if (statusCode == 404) {
                throw new ApiException("Erreur 404 Not Found de Twitch lors de la création d'une subscription Twitch.", e);
            } else if (statusCode == 429) {
                throw new ApiException("Erreur 429 Too Many Requests (rate limit) de Twitch lors de la création d'une subscription Twitch.", e);
            } else if (statusCode == 409) {
                log.info("Erreur 409 Conflict de Twitch lors de la création d'une subscription Twitch. Event {} déjà existant.", request.getType());
                throw new ApiBadRequestException(String.format(
                        "Erreur 409 Conflict de Twitch lors de la création d'une subscription Twitch. Event %s déjà existant.",
                        request.getType()
                ), e);
            } else {
                throw new ApiException(String.format(
                        "Erreur inconnue (code: %d) lors de la création d'une subscription Twitch.",
                        statusCode
                ), e);
            }
        }
    }

    public void deleteSubscription(@NonNull String subscriptionId) {
        try {
            this.eventSubReferenceClient.deleteSubscription(
                    super.addBearerPrefix(tokenService.fetchServerToken()),
                    subscriptionId
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }
}
