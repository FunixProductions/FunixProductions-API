package com.funixproductions.api.twitch.reference.client.dtos.responses.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TwitchFollowDTO {

    /**
     * The ID of the user that’s following the user in to_id.
     */
    @JsonProperty(value = "user_id")
    private String userId;

    /**
     * The follower’s login name.
     */
    @JsonProperty(value = "user_login")
    private String userLogin;

    /**
     * The follower’s display name.
     */
    @JsonProperty(value = "user_name")
    private String userName;

    /**
     * The UTC date and time (in RFC3339 format) of when the user in from_id began following the user in to_id.
     */
    @JsonProperty(value = "followed_at")
    private Date followedAt;

}
