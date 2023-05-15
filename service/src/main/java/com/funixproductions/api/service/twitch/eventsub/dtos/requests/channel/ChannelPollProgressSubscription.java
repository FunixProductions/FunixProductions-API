package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

public class ChannelPollProgressSubscription extends ChannelSubscription {

    public ChannelPollProgressSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POLL_PROGRESS.getType(), ChannelEventType.POLL_PROGRESS.getVersion());
    }

}
