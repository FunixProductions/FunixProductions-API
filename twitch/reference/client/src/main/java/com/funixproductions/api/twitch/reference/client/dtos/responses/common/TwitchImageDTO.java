package com.funixproductions.api.twitch.reference.client.dtos.responses.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchImageDTO {

    /**
     * The URL to a small version of the image.
     */
    @JsonProperty(value = "url_1x")
    private String urlSize1x;

    /**
     * The URL to a medium version of the image.
     */
    @JsonProperty(value = "url_2x")
    private String urlSize2x;

    /**
     * 	The URL to a large version of the image.
     */
    @JsonProperty(value = "url_4x")
    private String urlSize4x;

}
