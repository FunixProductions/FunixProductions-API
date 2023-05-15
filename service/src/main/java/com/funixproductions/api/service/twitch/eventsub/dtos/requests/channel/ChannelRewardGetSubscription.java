package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;

public class ChannelRewardGetSubscription extends ChannelSubscription {

    public ChannelRewardGetSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POINTS_REWARD_GET.getType(), ChannelEventType.POINTS_REWARD_GET.getVersion());
    }

}
