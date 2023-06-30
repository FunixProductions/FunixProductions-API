package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

public class ChannelPollBeginSubscription extends ChannelSubscription {
    public ChannelPollBeginSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POLL_BEGIN.getType(), ChannelEventType.POLL_BEGIN.getVersion());
    }
}
