package com.funixproductions.api.service.google.core.configs;

import com.funixproductions.api.service.google.auth.config.GoogleAuthConfig;
import com.funixproductions.api.service.google.gmail.config.GoogleGmailConfig;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
public class GoogleCoreConfig {

    @Bean
    public GoogleIdTokenVerifier verifier(GoogleGmailConfig googleGmailConfig,
                                          GoogleAuthConfig googleAuthConfig) {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(List.of(
                        googleAuthConfig.getClientId(),
                        googleGmailConfig.getClientId()
                )).build();
    }

}
