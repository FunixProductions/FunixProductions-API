package com.funixproductions.api.user.service.repositories;


import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.core.crud.repositories.ApiRepository;

import java.util.Optional;

public interface UserRepository extends ApiRepository<User> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameIgnoreCase(String username);
}
