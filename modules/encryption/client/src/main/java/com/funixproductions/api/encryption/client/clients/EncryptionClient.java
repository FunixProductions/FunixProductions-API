package com.funixproductions.api.encryption.client.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "EncryptionClient",
        url = "${funixproductions.api.encryption.app-domain-url}",
        path = "/encryption"
)
public interface EncryptionClient extends EncryptionClientDefinition {
}
