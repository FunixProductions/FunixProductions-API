package com.funixproductions.api.twitch.eventsub.service.repositories;

import com.funixproductions.api.twitch.eventsub.service.entities.TwitchEventSubStreamer;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwitchEventSubStreamerRepository extends ApiRepository<TwitchEventSubStreamer> {

    Optional<TwitchEventSubStreamer> findTwitchEventSubStreamerByStreamerId(String streamerId);
    void deleteTwitchEventSubStreamersByStreamerId(String streamerId);

}
