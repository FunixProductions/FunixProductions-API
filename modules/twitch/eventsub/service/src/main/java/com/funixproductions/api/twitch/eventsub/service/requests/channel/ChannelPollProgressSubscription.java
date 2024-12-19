package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelpollprogress">Doc</a>
 */
public class ChannelPollProgressSubscription extends AChannelSubscription {

    public ChannelPollProgressSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POLL_PROGRESS.getType(), ChannelEventType.POLL_PROGRESS.getVersion());
    }

}
