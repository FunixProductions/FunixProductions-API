package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelsubscriptionmessage">Doc</a>
 */
public class ChannelResubMessageSubscription extends ChannelSubscription {

    public ChannelResubMessageSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.RESUB_MESSAGE.getType(), ChannelEventType.RESUB_MESSAGE.getVersion());
    }

}
