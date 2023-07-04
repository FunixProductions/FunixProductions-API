package com.funixproductions.api.twitch.eventsub.service.requests.channel;


import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

public class ChannelHypeTrainEndSubscription extends ChannelSubscription {
    public ChannelHypeTrainEndSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.HYPE_TRAIN_END.getType(), ChannelEventType.HYPE_TRAIN_END.getVersion());
    }
}
