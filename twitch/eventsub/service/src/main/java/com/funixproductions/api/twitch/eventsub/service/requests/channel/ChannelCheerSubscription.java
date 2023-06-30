package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

public class ChannelCheerSubscription extends ChannelSubscription {

    public ChannelCheerSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.CHEER.getType(), ChannelEventType.CHEER.getVersion());
    }

}
