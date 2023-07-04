package com.funixproductions.api.twitch.eventsub.service.requests.channel;


import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

public class ChannelHypeTrainBeginSubscription extends ChannelSubscription {
    public ChannelHypeTrainBeginSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.HYPE_TRAIN_BEGIN.getType(), ChannelEventType.HYPE_TRAIN_BEGIN.getVersion());
    }
}
