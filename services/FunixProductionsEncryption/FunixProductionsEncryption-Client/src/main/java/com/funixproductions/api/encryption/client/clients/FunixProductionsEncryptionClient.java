package com.funixproductions.api.encryption.client.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "FunixProductionsEncryptionClient",
        url = "${funixproductions.api.encryption.app-domain-url}",
        path = "/encryption"
)
public interface FunixProductionsEncryptionClient {

    @PostMapping("encrypt")
    String encrypt(String clearText);

    @PostMapping("decrypt")
    String decrypt(String encryptedText);

}
