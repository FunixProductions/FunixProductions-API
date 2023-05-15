package com.funixproductions.api.client.twitch.reference.dtos.responses.channel.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchChannelChattersDTO {

    /**
     * The ID of a user that’s connected to the broadcaster’s chat room.
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof final TwitchChannelChattersDTO ent) {
            return ent.getUserId().equals(this.userId);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
