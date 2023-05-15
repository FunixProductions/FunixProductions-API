package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

public class ChannelPredictionEndSubscription extends ChannelSubscription {
    public ChannelPredictionEndSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.PREDICTION_END.getType(), ChannelEventType.PREDICTION_END.getVersion());
    }
}
