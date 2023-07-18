package com.funixproductions.api.encryption.client.utils;

import com.funixproductions.api.encryption.client.clients.FunixProductionsEncryptionClient;
import com.funixproductions.core.tools.encryption.ApiConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EncryptionString implements ApiConverter<String> {

    private final FunixProductionsEncryptionClient encryptionClient;

    /**
     * Encrypts the string
     * @param s the string to encrypt
     * @return the encrypted string
     */
    @Override
    public String convertToDatabaseColumn(String s) {
        try {
            return this.encryptionClient.encrypt(s);
        } catch (Exception e) {
            log.error("Error while encrypting text", e);
            throw e;
        }
    }

    /**
     * Decrypts the string
     * @param s the encrypted string
     * @return the decrypted string
     */
    @Override
    public String convertToEntityAttribute(String s) {
        try {
            return this.encryptionClient.decrypt(s);
        } catch (Exception e) {
            log.error("Error while decrypting text", e);
            throw e;
        }
    }
}
