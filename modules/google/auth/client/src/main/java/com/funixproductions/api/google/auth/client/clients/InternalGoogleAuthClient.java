package com.funixproductions.api.google.auth.client.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "InternalGoogleAuth",
        url = "${funixproductions.api.google.auth.app-domain-url}",
        path = "/kubeinternal/google/auth"
)
public interface InternalGoogleAuthClient {

    @DeleteMapping
    void deleteAllByUserUuidIn(@RequestBody List<String> userUuids);

}
