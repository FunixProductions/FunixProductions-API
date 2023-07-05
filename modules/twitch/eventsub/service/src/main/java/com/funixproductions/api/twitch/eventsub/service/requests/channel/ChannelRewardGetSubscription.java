package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

public class ChannelRewardGetSubscription extends ChannelSubscription {

    public ChannelRewardGetSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POINTS_REWARD_GET.getType(), ChannelEventType.POINTS_REWARD_GET.getVersion());
    }

}
