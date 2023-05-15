package com.funixproductions.api.client.twitch.reference.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchChannelUpdateDTO {

    /**
     * The ID of the game that the user plays. The game is not updated if the ID isn’t a game ID that Twitch recognizes.
     * To unset this field, use “0” or “” (an empty string).
     */
    @JsonProperty(value = "game_id")
    private String gameId;

    /**
     * The user’s preferred language. Set the value to an ISO 639-1 two-letter language code
     */
    @JsonProperty(value = "broadcaster_language")
    private String broadcasterLanguage;

    /**
     * The title of the user’s stream. You may not set this field to an empty string.
     */
    @JsonProperty(value = "title")
    private String title;

    /**
     * The number of seconds you want your broadcast buffered before streaming it live. The delay helps ensure fairness during competitive play.
     * Only users with Partner status may set this field. The maximum delay is 900 seconds (15 minutes).
     */
    @JsonProperty(value = "delay")
    private Integer delay;

}
