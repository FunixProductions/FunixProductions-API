package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelsubscriptiongift">Doc</a>
 */
public class ChannelSubGiftSubscription extends ChannelSubscription {
    public ChannelSubGiftSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.SUB_GIFT.getType(), ChannelEventType.SUB_GIFT.getVersion());
    }
}
