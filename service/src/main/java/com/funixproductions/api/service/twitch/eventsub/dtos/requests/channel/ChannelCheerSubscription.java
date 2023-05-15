package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

public class ChannelCheerSubscription extends ChannelSubscription {

    public ChannelCheerSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.CHEER.getType(), ChannelEventType.CHEER.getVersion());
    }

}
