package com.funixproductions.api.google.auth.service.resources;

import com.funixproductions.api.google.auth.client.clients.InternalGoogleAuthClient;
import com.funixproductions.api.google.auth.service.repositories.GoogleAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/kubeinternal/google/auth")
@RequiredArgsConstructor
public class InternalGoogleAuthResource implements InternalGoogleAuthClient {

    private final GoogleAuthRepository googleAuthRepository;

    @Override
    public void deleteAllByUserUuidIn(List<String> userUuids) {
        this.googleAuthRepository.deleteAllByUserUuidIn(userUuids);
        log.info("Deleted all google auths for users with uuids: {}", userUuids);
    }
}
