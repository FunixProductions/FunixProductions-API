package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelpredictionprogress">Doc</a>
 */
public class ChannelPredictionProgressSubscription extends AChannelSubscription {
    public ChannelPredictionProgressSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.PREDICTION_PROGRESS.getType(), ChannelEventType.PREDICTION_PROGRESS.getVersion());
    }
}
