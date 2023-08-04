package com.funixproductions.api.user.service.repositories;

import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.entities.UserValidAccountToken;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserValidAccountTokenRepository extends ApiRepository<UserValidAccountToken> {
    Optional<UserValidAccountToken> findByValidationToken(String validationToken);
    Optional<UserValidAccountToken> findByUser(User user);
}
