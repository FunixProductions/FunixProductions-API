package com.funixproductions.api.google.gmail.service.ressources;

import com.funixproductions.api.google.gmail.client.clients.GoogleGmailClient;
import com.funixproductions.api.google.gmail.client.dto.MailDTO;
import com.funixproductions.api.google.gmail.service.services.GoogleGmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/google/gmail")
public class GoogleGmailResource implements GoogleGmailClient {

    private final GoogleGmailService googleGmailService;

    @Override
    public void sendMail(MailDTO mailDTO, List<String> to) {
        googleGmailService.sendMail(mailDTO, to.toArray(new String[0]));
    }
}
