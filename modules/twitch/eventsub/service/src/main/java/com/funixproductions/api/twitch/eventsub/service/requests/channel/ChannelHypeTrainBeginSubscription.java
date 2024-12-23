package com.funixproductions.api.twitch.eventsub.service.requests.channel;


import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelhype_trainbegin">Doc</a>
 */
public class ChannelHypeTrainBeginSubscription extends AChannelSubscription {
    public ChannelHypeTrainBeginSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.HYPE_TRAIN_BEGIN.getType(), ChannelEventType.HYPE_TRAIN_BEGIN.getVersion());
    }
}
