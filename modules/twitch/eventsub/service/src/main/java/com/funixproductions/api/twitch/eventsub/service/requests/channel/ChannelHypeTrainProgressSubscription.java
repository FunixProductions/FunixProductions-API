package com.funixproductions.api.twitch.eventsub.service.requests.channel;


import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelhype_trainprogress">Doc</a>
 */
public class ChannelHypeTrainProgressSubscription extends AChannelSubscription {
    public ChannelHypeTrainProgressSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.HYPE_TRAIN_PROGRESS.getType(), ChannelEventType.HYPE_TRAIN_PROGRESS.getVersion());
    }
}
