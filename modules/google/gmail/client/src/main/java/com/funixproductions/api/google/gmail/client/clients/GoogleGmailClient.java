package com.funixproductions.api.google.gmail.client.clients;

import com.funixproductions.api.google.gmail.client.dto.MailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "GoogleGmailClient",
        url = "${funixproductions.api.google.gmail.app-domain-url}",
        path = "/google/gmail"
)
public interface GoogleGmailClient {

    @PostMapping("send")
    void sendMail(@RequestBody MailDTO mailDTO, @RequestParam("to") List<String> to);

}
