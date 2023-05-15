package com.funixproductions.api.client.twitch.reference.dtos.responses.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TwitchUserDTO {

    /**
     * An ID that identifies the user.
     */
    @JsonProperty(value = "id")
    private String id;

    /**
     * The user’s login name.
     */
    @JsonProperty(value = "login")
    private String login;

    /**
     * The user’s display name.
     */
    @JsonProperty(value = "display_name")
    private String displayName;

    /**
     * The type of user. Possible values are:
     * admin — Twitch administrator
     * global_mod
     * staff — Twitch staff
     * "" — Normal user
     */
    @JsonProperty(value = "type")
    private String type;

    /**
     * The type of broadcaster. Possible values are:
     * affiliate — An affiliate broadcaster
     * partner — A partner broadcaster
     * "" — A normal broadcaster
     */
    @JsonProperty(value = "broadcaster_type")
    private String broadcasterType;

    /**
     * The user’s description of their channel.
     */
    @JsonProperty(value = "description")
    private String description;

    /**
     * A URL to the user’s profile image.
     */
    @JsonProperty(value = "profile_image_url")
    private String profileImageUrl;

    /**
     * A URL to the user’s offline image.
     */
    @JsonProperty(value = "offline_image_url")
    private String offlineImageUrl;

    /**
     * The number of times the user’s channel has been viewed.
     */
    @JsonProperty(value = "view_count")
    private Integer viewCount;

    /**
     * The user’s verified email address. The object includes this field only if the user access token includes the user:read:email scope.
     */
    @JsonProperty(value = "email")
    private String email;

    /**
     * The UTC date and time that the user’s account was created. The timestamp is in RFC3339 format.
     */
    @JsonProperty(value = "created_at")
    private Date createdAt;

}
