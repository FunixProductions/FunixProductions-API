package com.funixproductions.api.client.twitch.eventsub.dtos.events.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TwitchEventChannelFollowDTO {

    @SerializedName(value = "broadcaster_user_id")
    @JsonProperty(value = "broadcaster_user_id")
    private String streamerId;

    @SerializedName(value = "broadcaster_user_login")
    @JsonProperty(value = "broadcaster_user_login")
    private String streamerUsername;

    @SerializedName(value = "broadcaster_user_name")
    @JsonProperty(value = "broadcaster_user_name")
    private String streamerDisplayName;

    @SerializedName(value = "user_id")
    @JsonProperty(value = "user_id")
    private String followerId;

    @SerializedName(value = "user_login")
    @JsonProperty(value = "user_login")
    private String followerName;

    @SerializedName(value = "user_name")
    @JsonProperty(value = "user_name")
    private String followerDisplayName;

    @SerializedName(value = "followed_at")
    @JsonProperty(value = "followed_at")
    private Date followedAt;

}
