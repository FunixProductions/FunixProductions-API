package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

public class ChannelPollBeginSubscription extends ChannelSubscription {
    public ChannelPollBeginSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POLL_BEGIN.getType(), ChannelEventType.POLL_BEGIN.getVersion());
    }
}
