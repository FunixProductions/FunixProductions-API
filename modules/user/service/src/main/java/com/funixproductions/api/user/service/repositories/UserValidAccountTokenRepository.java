package com.funixproductions.api.user.service.repositories;

import com.funixproductions.api.user.service.entities.UserValidAccountToken;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserValidAccountTokenRepository extends ApiRepository<UserValidAccountToken> {
    UserValidAccountToken findByValidationToken(String validationToken);
}
