package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

public class ChannelPredictionBeginSubscription extends ChannelSubscription {
    public ChannelPredictionBeginSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.PREDICTION_BEGIN.getType(), ChannelEventType.PREDICTION_BEGIN.getVersion());
    }
}
