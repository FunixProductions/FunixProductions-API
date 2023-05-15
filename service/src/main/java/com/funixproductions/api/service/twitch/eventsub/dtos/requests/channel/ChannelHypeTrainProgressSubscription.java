package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;


import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

public class ChannelHypeTrainProgressSubscription extends ChannelSubscription {
    public ChannelHypeTrainProgressSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.HYPE_TRAIN_PROGRESS.getType(), ChannelEventType.HYPE_TRAIN_PROGRESS.getVersion());
    }
}
