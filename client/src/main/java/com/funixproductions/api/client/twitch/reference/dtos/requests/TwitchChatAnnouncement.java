package com.funixproductions.api.client.twitch.reference.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TwitchChatAnnouncement {

    /**
     * The announcement to make in the broadcaster’s chat room.
     * Announcements are limited to a maximum of 500 characters;
     * announcements longer than 500 characters are truncated.
     */
    @JsonProperty(value = "message")
    private String message;

    /**
     * blue green orange purple primary
     */
    @JsonProperty(value = "message")
    private String color;

    public TwitchChatAnnouncement(final String message) {
        this.message = message;
        this.color = "primary";
    }

}
