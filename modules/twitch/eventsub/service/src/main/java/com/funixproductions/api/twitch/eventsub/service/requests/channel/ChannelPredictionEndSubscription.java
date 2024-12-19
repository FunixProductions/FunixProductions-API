package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelpredictionend">Doc</a>
 */
public class ChannelPredictionEndSubscription extends AChannelSubscription {
    public ChannelPredictionEndSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.PREDICTION_END.getType(), ChannelEventType.PREDICTION_END.getVersion());
    }
}
