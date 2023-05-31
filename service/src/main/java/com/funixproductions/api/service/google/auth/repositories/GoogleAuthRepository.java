package com.funixproductions.api.service.google.auth.repositories;

import com.funixproductions.api.service.google.auth.entities.GoogleAuthLinkUser;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoogleAuthRepository extends ApiRepository<GoogleAuthLinkUser> {
    Optional<GoogleAuthLinkUser> findByGoogleUserId(String googleUserId);
    void deleteAllByUserUuidIn(Iterable<String> userUuids);
}
