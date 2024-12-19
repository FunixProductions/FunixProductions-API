package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;
import com.google.gson.JsonObject;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelshoutoutcreate">Doc</a>
 */
public class ChannelShoutOutCreateSubscription extends AChannelSubscription {

    public ChannelShoutOutCreateSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.SHOUTOUT_CREATE.getType(), ChannelEventType.SHOUTOUT_CREATE.getVersion());
    }

    @Override
    protected JsonObject getCondition() {
        final JsonObject condition = new JsonObject();

        condition.addProperty("broadcaster_user_id", getStreamerId());
        condition.addProperty("moderator_user_id", getStreamerId());
        return condition;
    }
}
