package com.funixproductions.api.service.twitch.eventsub.dtos.requests.channel;

import com.funixproductions.api.service.twitch.eventsub.dtos.requests.TwitchSubscription;
import com.google.gson.JsonObject;

public abstract class ChannelSubscription extends TwitchSubscription {

    private final String streamerId;

    protected ChannelSubscription(String streamerId, String type, String version) {
        super(type, version);
        this.streamerId = streamerId;
    }

    @Override
    protected JsonObject getCondition() {
        final JsonObject condition = new JsonObject();

        condition.addProperty("broadcaster_user_id", this.streamerId);
        return condition;
    }

    public final String getStreamerId() {
        return streamerId;
    }
}
