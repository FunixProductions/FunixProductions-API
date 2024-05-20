package com.funixproductions.api.google.auth.service.repositories;

import com.funixproductions.api.google.auth.service.entities.GoogleAuthLinkUser;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface GoogleAuthRepository extends ApiRepository<GoogleAuthLinkUser> {
    Optional<GoogleAuthLinkUser> findByGoogleUserId(String googleUserId);

    void deleteAllByUserUuidIn(Collection<String> userUuid);
}
