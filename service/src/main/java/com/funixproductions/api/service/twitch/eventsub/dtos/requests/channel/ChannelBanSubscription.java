package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;


import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

public class ChannelBanSubscription extends ChannelSubscription {
    public ChannelBanSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.BAN.getType(), ChannelEventType.BAN.getVersion());
    }
}
