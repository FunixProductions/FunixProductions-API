package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelsubscriptiongift">Doc</a>
 */
public class ChannelSubGiftSubscription extends ChannelSubscription {
    public ChannelSubGiftSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.SUB_GIFT.getType(), ChannelEventType.SUB_GIFT.getVersion());
    }
}
