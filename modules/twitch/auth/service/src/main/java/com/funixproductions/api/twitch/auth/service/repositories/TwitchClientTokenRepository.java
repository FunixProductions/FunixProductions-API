package com.funixproductions.api.twitch.auth.service.repositories;

import com.funixproductions.api.twitch.auth.service.entities.TwitchClientToken;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwitchClientTokenRepository extends ApiRepository<TwitchClientToken> {
    Optional<TwitchClientToken> findTwitchClientTokenByUserUuid(String userUuid);
    Optional<TwitchClientToken> findTwitchClientTokenByTwitchUsername(String username);
    Optional<TwitchClientToken> findTwitchClientTokenByTwitchUserId(String streamerId);
}
