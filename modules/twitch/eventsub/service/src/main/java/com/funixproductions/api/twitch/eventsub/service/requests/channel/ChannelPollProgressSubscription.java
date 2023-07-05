package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

public class ChannelPollProgressSubscription extends ChannelSubscription {

    public ChannelPollProgressSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POLL_PROGRESS.getType(), ChannelEventType.POLL_PROGRESS.getVersion());
    }

}
