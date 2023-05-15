package com.funixproductions.api.service.twitch.auth.repositories;

import com.funixproductions.api.service.twitch.auth.entities.TwitchClientToken;
import com.funixproductions.api.service.user.entities.User;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwitchClientTokenRepository extends ApiRepository<TwitchClientToken> {
    Optional<TwitchClientToken> findTwitchClientTokenByUser(User user);
    Optional<TwitchClientToken> findTwitchClientTokenByTwitchUsername(String username);
}
