package com.funixproductions.api.twitch.eventsub.service.requests.channel;


import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelupdate">Documentation</a>
 */
public class ChannelUpdateSubscription extends AChannelSubscription {

    public ChannelUpdateSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.UPDATE.getType(), ChannelEventType.UPDATE.getVersion());
    }

}
