package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

public class ChannelPredictionEndSubscription extends ChannelSubscription {
    public ChannelPredictionEndSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.PREDICTION_END.getType(), ChannelEventType.PREDICTION_END.getVersion());
    }
}
