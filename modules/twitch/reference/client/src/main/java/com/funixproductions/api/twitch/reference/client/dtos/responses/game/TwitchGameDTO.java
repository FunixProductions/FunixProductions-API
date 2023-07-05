package com.funixproductions.api.twitch.reference.client.dtos.responses.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchGameDTO {

    /**
     * An ID that identifies the category or game.
     */
    @JsonProperty(value = "id")
    private String id;

    /**
     * The category’s or game’s name.
     */
    @JsonProperty(value = "name")
    private String name;

    /**
     * A URL to the category’s or game’s box art. You must replace the {width}x{height} placeholder with the size of image you want.
     */
    @JsonProperty(value = "box_art_url")
    private String boxArtUrl;

    public String getBoxArtUrl(final int width, final int height) {
        return this.boxArtUrl.replace("{width}", Integer.toString(width)).replace("{height}", Integer.toString(height));
    }

}
