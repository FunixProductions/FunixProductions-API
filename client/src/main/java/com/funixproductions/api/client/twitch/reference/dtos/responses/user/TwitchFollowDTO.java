package com.funixproductions.api.client.twitch.reference.dtos.responses.user;

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
    @JsonProperty(value = "from_id")
    private String fromId;

    /**
     * The follower’s login name.
     */
    @JsonProperty(value = "from_login")
    private String fromLogin;

    /**
     * The follower’s display name.
     */
    @JsonProperty(value = "from_name")
    private String fromName;

    /**
     * The ID of the user that’s being followed by the user in from_id.
     */
    @JsonProperty(value = "to_id")
    private String toId;

    /**
     * The login name of the user that’s being followed.
     */
    @JsonProperty(value = "to_login")
    private String toLogin;

    /**
     * The display name of the user that’s being followed.
     */
    @JsonProperty(value = "to_name")
    private String toName;

    /**
     * The UTC date and time (in RFC3339 format) of when the user in from_id began following the user in to_id.
     */
    @JsonProperty(value = "followed_at")
    private Date followedAt;

}
