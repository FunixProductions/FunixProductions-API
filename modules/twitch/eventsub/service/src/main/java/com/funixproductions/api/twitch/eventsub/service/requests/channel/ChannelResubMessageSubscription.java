package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelsubscriptionmessage">Doc</a>
 */
public class ChannelResubMessageSubscription extends AChannelSubscription {

    public ChannelResubMessageSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.RESUB_MESSAGE.getType(), ChannelEventType.RESUB_MESSAGE.getVersion());
    }

}
