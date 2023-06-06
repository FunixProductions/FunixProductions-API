package com.funixproductions.api.service.google.gmail.services;

import com.funixproductions.api.service.google.gmail.config.GoogleGmailConfig;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.exceptions.ApiForbiddenException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;

@Slf4j
@Service
public class GoogleGmailAuthService {

    private final GoogleGmailConfig googleTokensConfig;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    private final Gmail gmailService;

    public GoogleGmailAuthService(GoogleGmailConfig googleTokensConfig, GoogleIdTokenVerifier googleIdTokenVerifier) {
        this.googleTokensConfig = googleTokensConfig;
        this.googleIdTokenVerifier = googleIdTokenVerifier;

        final GoogleCredentials credentials = this.getCredentials();
        final HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
        this.gmailService = new Gmail.Builder(new NetHttpTransport(), new GsonFactory(), requestInitializer)
                .setApplicationName("FunixProductions Gmail Service")
                .build();
    }

    public void validateClient(String credential, String aud) {
        if (Strings.isNullOrEmpty(credential) || Strings.isNullOrEmpty(aud)) {
            throw new ApiBadRequestException("Credential and audience are required");
        }
        if (!aud.equals(this.googleTokensConfig.getClientId())) {
            throw new ApiForbiddenException("Invalid audience");
        }

        try {
            final GoogleIdToken token = this.googleIdTokenVerifier.verify(credential);

            if (token == null) {
                throw new ApiForbiddenException("Invalid credential");
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new ApiForbiddenException("Invalid credential");
        }
    }

    private GoogleCredentials getCredentials() {
        final File file = new File(GoogleGmailConfig.FILE_CREDENTIALS);

        try {
            if (file.exists()) {
                final byte[] data = Files.readAllBytes(file.toPath());
                return GoogleCredentials.fromStream(new ByteArrayInputStream(data));
            } else {
                throw new ApiException("Can't find gmail credentials file. Please import it to root folder of project.");
            }
        } catch (IOException e) {
            throw new ApiException("Can't read gmail credentials file. Please import it to root folder of project.", e);
        }
    }

    public Gmail getGmailService() {
        return gmailService;
    }
}
