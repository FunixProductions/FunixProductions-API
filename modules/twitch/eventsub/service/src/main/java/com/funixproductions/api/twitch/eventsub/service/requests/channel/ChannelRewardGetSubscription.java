package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelchannel_points_custom_reward_redemptionadd">Doc</a>
 */
public class ChannelRewardGetSubscription extends AChannelSubscription {

    public ChannelRewardGetSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POINTS_REWARD_GET.getType(), ChannelEventType.POINTS_REWARD_GET.getVersion());
    }

}
