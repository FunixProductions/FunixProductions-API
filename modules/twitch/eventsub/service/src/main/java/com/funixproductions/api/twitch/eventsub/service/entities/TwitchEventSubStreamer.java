package com.funixproductions.api.twitch.eventsub.service.entities;

import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "twitch_event_sub_streamers")
public class TwitchEventSubStreamer extends ApiEntity {

    /**
     * Streamer twitch id
     */
    @Column(nullable = false, unique = true, name = "streamer_id")
    private String streamerId;

}
