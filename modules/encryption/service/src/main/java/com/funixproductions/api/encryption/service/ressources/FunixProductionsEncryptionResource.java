package com.funixproductions.api.encryption.service.ressources;

import com.funixproductions.api.encryption.client.clients.EncryptionClientDefinition;
import com.funixproductions.api.encryption.service.services.FunixProductionsEncryptionService;
import com.funixproductions.core.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/encryption")
@RequiredArgsConstructor
public class FunixProductionsEncryptionResource implements EncryptionClientDefinition {

    private final FunixProductionsEncryptionService encryption;

    @Override
    public String encrypt(String clearText) {
        try {
            return this.encryption.convertToDatabase(clearText);
        } catch (ApiException e) {
            log.error("Error while encrypting text", e);
            throw e;
        }
    }

    @Override
    public String decrypt(String encryptedText) {
        try {
            return this.encryption.convertToEntity(encryptedText);
        } catch (ApiException e) {
            log.error("Error while decrypting text", e);
            throw e;
        }
    }
}
