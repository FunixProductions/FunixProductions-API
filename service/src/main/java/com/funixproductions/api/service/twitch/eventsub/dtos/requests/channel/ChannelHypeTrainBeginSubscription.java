package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;


import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

public class ChannelHypeTrainBeginSubscription extends ChannelSubscription {
    public ChannelHypeTrainBeginSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.HYPE_TRAIN_BEGIN.getType(), ChannelEventType.HYPE_TRAIN_BEGIN.getVersion());
    }
}
