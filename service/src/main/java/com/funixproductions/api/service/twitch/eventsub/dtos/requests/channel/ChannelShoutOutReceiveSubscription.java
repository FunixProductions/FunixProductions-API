package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.enums.ChannelEventType;
import com.google.gson.JsonObject;

public class ChannelShoutOutReceiveSubscription extends ChannelSubscription {

    public ChannelShoutOutReceiveSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.SHOUTOUT_RECEIVE.getType(), ChannelEventType.SHOUTOUT_RECEIVE.getVersion());
    }

    @Override
    protected JsonObject getCondition() {
        final JsonObject condition = new JsonObject();

        condition.addProperty("broadcaster_user_id", getStreamerId());
        condition.addProperty("moderator_user_id", getStreamerId());
        return condition;
    }
}
