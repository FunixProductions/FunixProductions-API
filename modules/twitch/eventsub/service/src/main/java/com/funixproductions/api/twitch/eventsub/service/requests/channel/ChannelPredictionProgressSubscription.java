package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

public class ChannelPredictionProgressSubscription extends ChannelSubscription {
    public ChannelPredictionProgressSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.PREDICTION_PROGRESS.getType(), ChannelEventType.PREDICTION_PROGRESS.getVersion());
    }
}
