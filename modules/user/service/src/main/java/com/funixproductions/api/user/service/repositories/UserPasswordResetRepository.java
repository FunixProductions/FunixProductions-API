package com.funixproductions.api.user.service.repositories;

import com.funixproductions.api.user.service.entities.UserPasswordReset;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordResetRepository extends ApiRepository<UserPasswordReset> {
}
