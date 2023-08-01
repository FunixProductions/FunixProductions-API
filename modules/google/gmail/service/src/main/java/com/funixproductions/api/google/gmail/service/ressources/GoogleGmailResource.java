package com.funixproductions.api.google.gmail.service.ressources;

import com.funixproductions.api.google.gmail.client.clients.GoogleGmailClient;
import com.funixproductions.api.google.gmail.client.dto.MailDTO;
import com.funixproductions.api.google.gmail.service.services.GoogleGmailService;
import com.funixproductions.core.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/google/gmail")
public class GoogleGmailResource implements GoogleGmailClient {

    private final GoogleGmailService googleGmailService;

    @Override
    public void sendMail(MailDTO mailDTO, List<String> to) {
        try {
            googleGmailService.sendMail(mailDTO, to.toArray(new String[0]));
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            final String message = "Une erreur interne est survenue lors de l'envoi du mail.";

            log.error(message, e);
            throw new ApiException(message, e);
        }
    }
}
