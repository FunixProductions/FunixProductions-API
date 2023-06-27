package com.funixproductions.api.encryption.client.utils;

import com.funixproductions.api.encryption.client.clients.FunixProductionsEncryptionClient;
import com.funixproductions.core.tools.encryption.ApiConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EncryptionString implements ApiConverter<String> {

    private final FunixProductionsEncryptionClient encryptionClient;

    @Override
    public String convertToDatabaseColumn(String s) {
        return encryptionClient.encrypt(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return encryptionClient.decrypt(s);
    }
}
