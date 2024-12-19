package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelpredictionbegin">Doc</a>
 */
public class ChannelPredictionBeginSubscription extends AChannelSubscription {
    public ChannelPredictionBeginSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.PREDICTION_BEGIN.getType(), ChannelEventType.PREDICTION_BEGIN.getVersion());
    }
}
