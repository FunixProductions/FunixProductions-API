package com.funixproductions.api.encryption.service.ressources;

import com.funixproductions.api.encryption.client.clients.FunixProductionsEncryptionClient;
import com.funixproductions.api.encryption.service.services.FunixProductionsEncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/encryption")
@RequiredArgsConstructor
public class FunixProductionsEncryptionResource implements FunixProductionsEncryptionClient {

    private final FunixProductionsEncryptionService encryption;

    @Override
    public String encrypt(String clearText) {
        return this.encryption.convertToDatabase(clearText);
    }

    @Override
    public String decrypt(String encryptedText) {
        return this.encryption.convertToEntity(encryptedText);
    }
}
