package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelupdate">Documentation</a>
 */
public class ChannelUpdateSubscription extends ChannelSubscription {

    public ChannelUpdateSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.UPDATE.getType(), ChannelEventType.UPDATE.getVersion());
    }

}
