package com.funixproductions.api.twitch.eventsub.service.requests.channel;

import com.funixproductions.api.twitch.eventsub.service.requests.TwitchSubscription;
import com.google.gson.JsonObject;

public abstract class AChannelSubscription extends TwitchSubscription {

    private final String streamerId;

    protected AChannelSubscription(String streamerId, String type, String version) {
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
