package com.funixproductions.api.twitch.auth.service.repositories;

import com.funixproductions.api.twitch.auth.client.enums.TwitchClientTokenType;
import com.funixproductions.api.twitch.auth.service.entities.TwitchClientToken;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwitchClientTokenRepository extends ApiRepository<TwitchClientToken> {
    Optional<TwitchClientToken> findTwitchClientTokenByUserUuidAndTokenType(String userUuid, TwitchClientTokenType tokenType);
    Optional<TwitchClientToken> findTwitchClientTokenByTwitchUsernameAndTokenType(String username, TwitchClientTokenType tokenType);
    Optional<TwitchClientToken> findTwitchClientTokenByTwitchUserIdAndTokenType(String streamerId, TwitchClientTokenType tokenType);
}
