package com.funixproductions.api.encryption.client.clients;

import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "FunixProductionsEncryptionClient",
        url = "${funixproductions.api.encryption.app-domain-url}",
        path = "/encryption"
)
public interface FunixProductionsEncryptionClient {

    @PostMapping("encrypt")
    String encrypt(@RequestBody @NotBlank String clearText);

    @PostMapping("decrypt")
    String decrypt(@RequestBody @NotBlank String encryptedText);

}
