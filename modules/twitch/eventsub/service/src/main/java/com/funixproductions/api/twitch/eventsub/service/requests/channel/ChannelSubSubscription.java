package com.funixproductions.api.twitch.eventsub.service.requests.channel;


import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

public class ChannelSubSubscription extends ChannelSubscription {

    public ChannelSubSubscription(String streamerId) {
        super(streamerId, ChannelEventType.SUBSCRIPTION.getType(), ChannelEventType.SUBSCRIPTION.getVersion());
    }

}
