package com.funixproductions.api.twitch.eventsub.service.requests.channel;


import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

public class ChannelHypeTrainProgressSubscription extends ChannelSubscription {
    public ChannelHypeTrainProgressSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.HYPE_TRAIN_PROGRESS.getType(), ChannelEventType.HYPE_TRAIN_PROGRESS.getVersion());
    }
}
