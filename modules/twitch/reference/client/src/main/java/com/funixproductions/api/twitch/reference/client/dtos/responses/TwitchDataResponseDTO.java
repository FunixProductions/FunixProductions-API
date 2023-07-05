package com.funixproductions.api.twitch.reference.client.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.funixproductions.api.twitch.reference.client.dtos.responses.common.TwitchPaginationDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TwitchDataResponseDTO<T> {

    /**
     * Nevel null
     * data list
     */
    @JsonProperty(value = "data")
    private List<T> data;

    /**
     * sometimes null, not all endpoints give this
     */
    @JsonProperty(value = "pagination")
    private TwitchPaginationDTO pagination;

    /**
     * sometimes null, not all endpoints give this
     */
    @JsonProperty(value = "total")
    private Integer total;

}
