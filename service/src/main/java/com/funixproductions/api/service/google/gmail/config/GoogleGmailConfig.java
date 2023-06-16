package com.funixproductions.api.service.google.gmail.config;

import com.funixproductions.core.exceptions.ApiException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Getter
@Setter
@Profile("!test")
@Configuration
@ConfigurationProperties("google.clients.gmail")
public class GoogleGmailConfig {

    private static final String FILE_CREDENTIALS = "gmail-credentials.json";

    /**
     * App sender email, must be verified in google console
     */
    private String appEmail;

    /**
     * Disable when test env
     */
    private Boolean testMode = false;

    @Bean
    public GoogleCredentials googleCredentials() {
        final File file = new File(FILE_CREDENTIALS);

        try (FileInputStream inputStream = new FileInputStream(file)) {
            if (file.exists()) {
                return GoogleCredentials.fromStream(inputStream).createScoped(GmailScopes.GMAIL_SEND).createDelegated(this.appEmail);
            } else {
                throw new ApiException("Can't find gmail credentials file. Please import it to root folder of project.");
            }
        } catch (IOException e) {
            throw new ApiException("Can't read gmail credentials file. Please import it to root folder of project.", e);
        }
    }

    @Bean
    public Gmail gmail(GoogleCredentials googleCredentials) {
        final HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(googleCredentials);
        return new Gmail.Builder(new NetHttpTransport(), new GsonFactory(), requestInitializer)
                .setApplicationName("FunixProductions Gmail Service")
                .build();
    }

}
