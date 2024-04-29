package com.funixproductions.api.user.service.security;

import com.funixproductions.core.tools.encryption.ApiConverter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionBase64 implements ApiConverter<String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return Base64.getEncoder().encodeToString(attribute.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return new String(Base64.getDecoder().decode(dbData), StandardCharsets.UTF_8);
    }
}
