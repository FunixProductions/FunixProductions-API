package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelsubscriptionmessage">Doc</a>
 */
public class ChannelResubMessageSubscription extends ChannelSubscription {

    public ChannelResubMessageSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.RESUB_MESSAGE.getType(), ChannelEventType.RESUB_MESSAGE.getVersion());
    }

}
