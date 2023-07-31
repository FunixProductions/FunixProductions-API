package com.funixproductions.api.user.service.ressources;

import com.funixproductions.api.google.gmail.client.clients.GoogleGmailClient;
import com.funixproductions.api.google.recaptcha.client.clients.GoogleRecaptchaInternalClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserResetPasswordResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoogleRecaptchaInternalClient captchaService;

    @MockBean
    private GoogleGmailClient gmailClient;

}
