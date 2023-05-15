package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

public class ChannelPollEndSubscription extends ChannelSubscription {
    public ChannelPollEndSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POLL_END.getType(), ChannelEventType.POLL_END.getVersion());
    }
}