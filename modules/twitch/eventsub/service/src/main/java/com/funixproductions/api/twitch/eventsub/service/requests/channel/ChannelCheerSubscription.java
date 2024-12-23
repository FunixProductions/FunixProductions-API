package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelcheer">Doc</a>
 */
public class ChannelCheerSubscription extends AChannelSubscription {

    public ChannelCheerSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.CHEER.getType(), ChannelEventType.CHEER.getVersion());
    }

}
