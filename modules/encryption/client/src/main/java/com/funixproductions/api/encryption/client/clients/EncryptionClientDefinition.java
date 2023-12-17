package com.funixproductions.api.encryption.client.clients;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface EncryptionClientDefinition {

    @PostMapping("encrypt")
    String encrypt(@RequestBody @NotBlank String clearText);

    @PostMapping("decrypt")
    String decrypt(@RequestBody @NotBlank String encryptedText);

}
