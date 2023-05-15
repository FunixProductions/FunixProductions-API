package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;


import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

public class ChannelHypeTrainEndSubscription extends ChannelSubscription {
    public ChannelHypeTrainEndSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.HYPE_TRAIN_END.getType(), ChannelEventType.HYPE_TRAIN_END.getVersion());
    }
}
