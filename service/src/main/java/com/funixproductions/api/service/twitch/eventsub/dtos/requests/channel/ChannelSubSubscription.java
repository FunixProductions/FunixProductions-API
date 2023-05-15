package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

public class ChannelSubSubscription extends ChannelSubscription {

    public ChannelSubSubscription(String streamerId) {
        super(streamerId, ChannelEventType.SUBSCRIPTION.getType(), ChannelEventType.SUBSCRIPTION.getVersion());
    }

}
