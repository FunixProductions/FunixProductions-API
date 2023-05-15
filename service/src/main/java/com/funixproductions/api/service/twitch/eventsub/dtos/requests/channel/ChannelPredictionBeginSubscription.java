package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

public class ChannelPredictionBeginSubscription extends ChannelSubscription {
    public ChannelPredictionBeginSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.PREDICTION_BEGIN.getType(), ChannelEventType.PREDICTION_BEGIN.getVersion());
    }
}
