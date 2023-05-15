package com.funixproductions.api.service.user.repositories;

import com.funixproductions.api.service.user.entities.UserToken;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends ApiRepository<UserToken> {
}
