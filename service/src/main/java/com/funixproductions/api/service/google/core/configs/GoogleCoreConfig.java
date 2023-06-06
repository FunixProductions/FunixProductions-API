package com.funixproductions.api.service.google.core.configs;

import com.funixproductions.api.service.google.auth.config.GoogleAuthConfig;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiForbiddenException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
public class GoogleCoreConfig {

    @Bean
    public GoogleIdTokenVerifier verifier(GoogleAuthConfig googleAuthConfig) {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(List.of(
                        googleAuthConfig.getClientId()
                )).build();
    }

    public void checkAudience(final @NonNull GoogleIdToken idToken, final @NonNull String clientId) {
        if (idToken.getPayload().getAudience() instanceof final String aud) {
            if (Strings.isNullOrEmpty(aud)) {
                throw new ApiBadRequestException("Audience is required");
            } else {
                if (!clientId.equals(aud)) {
                    throw new ApiForbiddenException("Invalid audience");
                }
            }
        } else {
            throw new ApiBadRequestException("Audience is required");
        }
    }

}
