package com.funixproductions.api.google.auth.client.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;

@FeignClient(
        name = "InternalGoogleAuth",
        url = "${funixproductions.api.google.auth.app-domain-url}",
        path = "/kubeinternal/google/auth"
)
public interface InternalGoogleAuthClient {

    @DeleteMapping
    void deleteAllByUserUuidIn(Iterable<String> userUuids);

}
