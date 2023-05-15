package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;
import com.google.gson.JsonObject;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelfollow">Documentation</a>
 */
public class ChannelFollowSubscription extends ChannelSubscription {

    public ChannelFollowSubscription(String streamerId) {
        super(streamerId, ChannelEventType.FOLLOW.getType(), ChannelEventType.FOLLOW.getVersion());
    }

    @Override
    protected JsonObject getCondition() {
        final JsonObject condition = new JsonObject();

        condition.addProperty("broadcaster_user_id", super.getStreamerId());
        condition.addProperty("moderator_user_id", super.getStreamerId());
        return condition;
    }
}
