package com.funixproductions.api.twitch.eventsub.service.requests.channel;


import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelsubscribe">Doc</a>
 */
public class ChannelSubSubscription extends AChannelSubscription {

    public ChannelSubSubscription(String streamerId) {
        super(streamerId, ChannelEventType.SUBSCRIPTION.getType(), ChannelEventType.SUBSCRIPTION.getVersion());
    }

}
