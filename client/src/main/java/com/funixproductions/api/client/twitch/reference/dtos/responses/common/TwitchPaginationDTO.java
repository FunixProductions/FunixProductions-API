package com.funixproductions.api.client.twitch.reference.dtos.responses.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchPaginationDTO {

    /**
     * The cursor used to get the next page of results.
     * Use the cursor to set the request’s after query parameter.
     */
    @JsonProperty(value = "cursor")
    private String cursor;

}
