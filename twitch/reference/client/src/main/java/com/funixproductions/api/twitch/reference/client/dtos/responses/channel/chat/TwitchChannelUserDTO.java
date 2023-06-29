package com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchChannelUserDTO {

    /**
     * The ID of the user of the broadcaster’s channel.
     */
    @JsonProperty(value = "user_id")
    private String userId;

    /**
     * The user’s login name.
     */
    @JsonProperty(value = "user_login")
    private String userLogin;

    /**
     * The user’s display name.
     */
    @JsonProperty(value = "user_name")
    private String userName;

}
