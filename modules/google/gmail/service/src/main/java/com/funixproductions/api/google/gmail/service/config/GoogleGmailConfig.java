package com.funixproductions.api.google.gmail.service.config;

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

/**
 * Needs env GOOGLE_APPLICATION_CREDENTIALS
 * to set path to google service account json file
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("google.gmail")
public class GoogleGmailConfig {

    /**
     * App sender email, must be verified in google console
     */
    private String appEmail = "noreply@funixproductions.com";

    /**
     * App sender display name
     */
    private String appEmailName = "FunixProductions";

    @Bean
    public GoogleCredentials googleCredentials() throws Exception {
        return GoogleCredentials.getApplicationDefault().createScoped(GmailScopes.GMAIL_SEND).createDelegated(this.appEmail);
    }

    @Bean
    public Gmail gmail(GoogleCredentials googleCredentials) {
        return new Gmail.Builder(new NetHttpTransport(), new GsonFactory(), new HttpCredentialsAdapter(googleCredentials))
                .setApplicationName("FunixProductions Gmail Service")
                .build();
    }

}
