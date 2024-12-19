package com.funixproductions.api.twitch.eventsub.service.requests.channel;


import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelhype_trainend">Doc</a>
 */
public class ChannelHypeTrainEndSubscription extends AChannelSubscription {
    public ChannelHypeTrainEndSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.HYPE_TRAIN_END.getType(), ChannelEventType.HYPE_TRAIN_END.getVersion());
    }
}
