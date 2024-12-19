package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelpollbegin">Doc</a>
 */
public class ChannelPollBeginSubscription extends AChannelSubscription {
    public ChannelPollBeginSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POLL_BEGIN.getType(), ChannelEventType.POLL_BEGIN.getVersion());
    }
}
