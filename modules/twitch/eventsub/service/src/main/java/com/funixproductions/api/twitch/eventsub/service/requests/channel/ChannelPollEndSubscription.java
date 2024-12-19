package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelpollend">Doc</a>
 */
public class ChannelPollEndSubscription extends AChannelSubscription {
    public ChannelPollEndSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POLL_END.getType(), ChannelEventType.POLL_END.getVersion());
    }
}