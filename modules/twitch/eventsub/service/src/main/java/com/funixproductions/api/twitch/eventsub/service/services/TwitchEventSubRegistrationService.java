package com.funixproductions.api.twitch.eventsub.service.services;

import com.funixproductions.api.twitch.eventsub.client.dtos.TwitchEventSubListDTO;
import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;
import com.funixproductions.api.twitch.eventsub.service.enums.TwitchEventStatus;
import com.funixproductions.api.twitch.eventsub.service.requests.TwitchSubscription;
import com.funixproductions.api.twitch.eventsub.service.requests.channel.AChannelSubscription;
import com.funixproductions.api.twitch.reference.client.clients.users.TwitchUsersClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchUserDTO;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Service dedicated to the handling of register and removing streamer subscriptions events
 * Also used to check the valid status of the registered events
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TwitchEventSubRegistrationService {

    private final TwitchEventSubReferenceService twitchEventSubReferenceService;
    private final TwitchUsersClient twitchUsersClient;

    private final Queue<TwitchSubscription> subscriptions = new LinkedList<>();
    private final Queue<String> unsubscriptions = new LinkedList<>();

    /**
     * Method called by the ressource to create the event list subscriptions for a streamer
     * Calls inside an async function who process the task (high CPU demand due to delay to not spamm twitch api)
     * @param streamerUsername streamer name like funixgaming to create his subscriptions
     * @throws ApiBadRequestException when error
     */
    public void createSubscription(final String streamerUsername) {
        final String streamerId = this.getUserIdFromUsername(streamerUsername);

        this.subscriptions.addAll(
                this.generateChannelSubscriptions(streamerId)
        );
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    public void handleOnePendingSubscriptionFromQueue() {
        final TwitchSubscription subscription = this.subscriptions.poll();

        if (subscription != null) {
            this.twitchEventSubReferenceService.createSubscription(subscription);
            log.info("Subscription created for type {} with payload {}.", subscription.getType(), subscription.getPayload());
        }
    }

    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.SECONDS)
    public void handleOnePendingUnsubscriptionFromQueue() {
        final String subscriptionId = this.unsubscriptions.poll();

        if (subscriptionId != null) {
            this.twitchEventSubReferenceService.deleteSubscription(subscriptionId);
            log.info("Subscription deleted with id {}.", subscriptionId);
        }
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void fetchEventsThatNeedUpdate() {
        final List<TwitchEventSubListDTO.TwitchEventSub> events = this.getEventsThatNeedUpdate();
        if (events.isEmpty()) {
            return;
        }

        for (TwitchEventSubListDTO.TwitchEventSub event : events) {
            final TwitchSubscription subscription = this.generateSubscriptionFromEvent(event);

            if (subscription != null) {
                this.subscriptions.add(subscription);
                this.unsubscriptions.add(event.getId());
            }
        }
    }

    @Nullable
    private TwitchSubscription generateSubscriptionFromEvent(TwitchEventSubListDTO.TwitchEventSub event) {
        final TwitchEventSubListDTO.Condition condition = event.getCondition();
        if (condition == null) {
            return null;
        }
        final String streamerId = condition.getStreamerId();
        if (streamerId == null) {
            return null;
        }

        final List<TwitchSubscription> subscriptions = this.generateChannelSubscriptions(streamerId);

        for (final TwitchSubscription subscription : subscriptions) {
            if (subscription.getType().equals(event.getType())) {
                return subscription;
            }
        }

        return null;
    }

    private String getUserIdFromUsername(final String username) throws ApiBadRequestException {
        try {
            final TwitchDataResponseDTO<TwitchUserDTO> userList = this.twitchUsersClient.getUsersByName(List.of(username));

            try {
                return userList.getData().getFirst().getId();
            } catch (NoSuchElementException e) {
                throw new ApiBadRequestException(String.format("Le streamer %s n'existe pas sur twitch.", username));
            }
        } catch (FeignException e) {
            throw new ApiException(String.format("Erreur interne lors de la récupération de l'id du streamer %s.", username), e);
        }
    }

    private List<TwitchSubscription> generateChannelSubscriptions(final String streamerId) {
        try {
            final List<TwitchSubscription> subscriptions = new ArrayList<>();

            for (ChannelEventType eventType : ChannelEventType.values()) {
                final Constructor<? extends AChannelSubscription> constructor = eventType.getClazz().getConstructor(String.class);
                final AChannelSubscription channelSubscription = constructor.newInstance(streamerId);
                subscriptions.add(channelSubscription);
            }

            return subscriptions;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ApiException(String.format("Erreur interne lors de la création de twitch events pour le streamer %s.", streamerId), e);
        }
    }

    private List<TwitchEventSubListDTO.TwitchEventSub> getEventsThatNeedUpdate() throws ApiException {
        try {
            final List<TwitchEventSubListDTO.TwitchEventSub> toSend = new ArrayList<>();

            TwitchEventSubListDTO subs = this.twitchEventSubReferenceService.getSubscriptions(
                    TwitchEventStatus.VERSION_REMOVED.getStatus(), null, null, null
            );
            String pagination;

            while (true) {
                toSend.addAll(subs.getData());
                pagination = subs.hasPagination();

                if (pagination == null) {
                    break;
                } else {
                    subs = this.twitchEventSubReferenceService.getSubscriptions(
                            TwitchEventStatus.VERSION_REMOVED.getStatus(), null, null, pagination
                    );
                }
            }

            return toSend;
        } catch (Exception e) {
            throw new ApiException("Erreur lors de la récupération des events Twitch qui demandent une mise à jour.", e);
        }
    }

}
