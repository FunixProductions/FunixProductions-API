package com.funixproductions.api.twitch.reference.client.dtos.responses.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchChannelDTO {

    /**
     * An ID that uniquely identifies the broadcaster.
     */
    @JsonProperty(value = "broadcaster_id")
    private String broadcasterId;

    /**
     * The broadcaster’s login name.
     */
    @JsonProperty(value = "broadcaster_login")
    private String broadcasterLogin;

    /**
     * The broadcaster’s display name.
     */
    @JsonProperty(value = "broadcaster_name")
    private String broadcasterName;

    /**
     * The broadcaster’s preferred language. The value is an ISO 639-1 two-letter language code
     */
    @JsonProperty(value = "broadcaster_language")
    private String broadcasterLanguage;

    /**
     * The name of the game that the broadcaster is playing or last played. The value is an empty string if the broadcaster has never played a game.
     */
    @JsonProperty(value = "game_name")
    private String gameName;

    /**
     * An ID that uniquely identifies the game that the broadcaster is playing or last played. The value is an empty string if the broadcaster has never played a game.
     */
    @JsonProperty(value = "game_id")
    private String gameId;

    /**
     * The title of the stream that the broadcaster is currently streaming or last streamed. The value is an empty string if the broadcaster has never streamed.
     */
    @JsonProperty(value = "title")
    private String title;

    /**
     * The value of the broadcaster’s stream delay setting, in seconds. Reserved for users with Partner status.
     */
    @JsonProperty(value = "delay")
    private Integer delay;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof final TwitchChannelDTO ent) {
            return ent.getBroadcasterId().equals(this.broadcasterId);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
