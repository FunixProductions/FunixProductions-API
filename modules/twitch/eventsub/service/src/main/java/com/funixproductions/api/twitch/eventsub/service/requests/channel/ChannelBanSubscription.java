package com.funixproductions.api.twitch.eventsub.service.requests.channel;


import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

public class ChannelBanSubscription extends ChannelSubscription {
    public ChannelBanSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.BAN.getType(), ChannelEventType.BAN.getVersion());
    }
}
