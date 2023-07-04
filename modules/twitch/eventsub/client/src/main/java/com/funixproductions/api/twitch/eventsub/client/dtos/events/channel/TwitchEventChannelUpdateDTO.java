package com.funixproductions.api.twitch.eventsub.client.dtos.events.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchEventChannelUpdateDTO {

    @SerializedName(value = "broadcaster_user_id")
    @JsonProperty(value = "broadcaster_user_id")
    private String streamerId;

    @SerializedName(value = "broadcaster_user_login")
    @JsonProperty(value = "broadcaster_user_login")
    private String streamerUsername;

    @SerializedName(value = "broadcaster_user_name")
    @JsonProperty(value = "broadcaster_user_name")
    private String streamerDisplayName;

    @SerializedName(value = "title")
    @JsonProperty(value = "title")
    private String title;

    @SerializedName(value = "category_id")
    @JsonProperty(value = "category_id")
    private String categoryId;

    @SerializedName(value = "category_name")
    @JsonProperty(value = "category_name")
    private String categoryName;

}
