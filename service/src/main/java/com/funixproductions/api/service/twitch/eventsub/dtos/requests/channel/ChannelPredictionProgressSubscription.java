package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

public class ChannelPredictionProgressSubscription extends ChannelSubscription {
    public ChannelPredictionProgressSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.PREDICTION_PROGRESS.getType(), ChannelEventType.PREDICTION_PROGRESS.getVersion());
    }
}
