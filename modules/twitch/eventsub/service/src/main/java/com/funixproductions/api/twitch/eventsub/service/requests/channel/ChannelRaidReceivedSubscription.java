package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.enums.ChannelEventType;
import com.google.gson.JsonObject;

public class ChannelRaidReceivedSubscription extends ChannelSubscription {
    public ChannelRaidReceivedSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.RAID_RECEIVED.getType(), ChannelEventType.RAID_RECEIVED.getVersion());
    }

    @Override
    protected JsonObject getCondition() {
        final JsonObject condition = new JsonObject();

        condition.addProperty("to_broadcaster_user_id", getStreamerId());
        return condition;
    }
}
