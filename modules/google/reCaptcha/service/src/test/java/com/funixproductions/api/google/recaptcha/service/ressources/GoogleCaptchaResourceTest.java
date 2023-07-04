package com.funixproductions.api.google.recaptcha.service.ressources;

import com.funixproductions.api.google.recaptcha.client.services.GoogleRecaptchaHandler;
import com.funixproductions.api.google.recaptcha.service.clients.GoogleRecaptchaClient;
import com.funixproductions.api.google.recaptcha.service.dtos.GoogleCaptchaSiteVerifyResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class GoogleCaptchaResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoogleRecaptchaClient googleRecaptchaClient;

    @Test
    void testVerifySuccess() throws Exception {
        final GoogleCaptchaSiteVerifyResponseDTO responseDTO = new GoogleCaptchaSiteVerifyResponseDTO();
        responseDTO.setSuccess(true);
        responseDTO.setAction("test");
        responseDTO.setHostname("56.3.4.12");
        responseDTO.setScore(0.9f);
        setupMock(responseDTO);

        mockMvc.perform(post("/google/recaptcha/verify")
                        .header(GoogleRecaptchaHandler.GOOGLE_ACTION_HEADER, "test")
                        .header(GoogleRecaptchaHandler.GOOGLE_CODE_HEADER, "test")
                        .header("X-FORWARDED-FOR", responseDTO.getHostname())
                ).andExpect(status().isOk());

        verify(this.googleRecaptchaClient, times(1)).verify(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testVerifyFailGoogle() throws Exception {
        final GoogleCaptchaSiteVerifyResponseDTO responseDTO = new GoogleCaptchaSiteVerifyResponseDTO();
        responseDTO.setSuccess(true);
        responseDTO.setAction("test");
        responseDTO.setHostname("56.3.4.12");
        responseDTO.setScore(0.1f);
        setupMock(responseDTO);

        mockMvc.perform(post("/google/recaptcha/verify")
                .header(GoogleRecaptchaHandler.GOOGLE_ACTION_HEADER, "test")
                .header(GoogleRecaptchaHandler.GOOGLE_CODE_HEADER, "test")
                .header("X-FORWARDED-FOR", responseDTO.getHostname())
        ).andExpect(status().isBadRequest());

        verify(this.googleRecaptchaClient, times(1)).verify(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testVerifyFailGoogle2() throws Exception {
        final GoogleCaptchaSiteVerifyResponseDTO responseDTO = new GoogleCaptchaSiteVerifyResponseDTO();
        responseDTO.setSuccess(false);
        responseDTO.setAction("test");
        responseDTO.setHostname("56.3.4.12");
        responseDTO.setScore(0.8f);
        setupMock(responseDTO);

        mockMvc.perform(post("/google/recaptcha/verify")
                .header(GoogleRecaptchaHandler.GOOGLE_ACTION_HEADER, "test")
                .header(GoogleRecaptchaHandler.GOOGLE_CODE_HEADER, "test")
                .header("X-FORWARDED-FOR", responseDTO.getHostname())
        ).andExpect(status().isBadRequest());

        verify(this.googleRecaptchaClient, times(1)).verify(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testVerifyFailGoogle3() throws Exception {
        final GoogleCaptchaSiteVerifyResponseDTO responseDTO = new GoogleCaptchaSiteVerifyResponseDTO();
        responseDTO.setSuccess(true);
        responseDTO.setAction("test2");
        responseDTO.setHostname("56.3.4.12");
        responseDTO.setScore(0.8f);
        setupMock(responseDTO);

        mockMvc.perform(post("/google/recaptcha/verify")
                .header(GoogleRecaptchaHandler.GOOGLE_ACTION_HEADER, "test")
                .header(GoogleRecaptchaHandler.GOOGLE_CODE_HEADER, "test")
                .header("X-FORWARDED-FOR", responseDTO.getHostname())
        ).andExpect(status().isBadRequest());

        verify(this.googleRecaptchaClient, times(1)).verify(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testVerifyFailGoogle4() throws Exception {
        final GoogleCaptchaSiteVerifyResponseDTO responseDTO = new GoogleCaptchaSiteVerifyResponseDTO();
        responseDTO.setSuccess(true);
        responseDTO.setAction("test");
        responseDTO.setHostname("56.3.4.12");
        responseDTO.setScore(0.1f);
        setupMock(responseDTO);

        mockMvc.perform(post("/google/recaptcha/verify")
                .header(GoogleRecaptchaHandler.GOOGLE_ACTION_HEADER, "test")
                .header(GoogleRecaptchaHandler.GOOGLE_CODE_HEADER, "test")
                .header("X-FORWARDED-FOR", responseDTO.getHostname())
        ).andExpect(status().isBadRequest());

        verify(this.googleRecaptchaClient, times(1)).verify(anyString(), anyString(), anyString(), anyString());
    }

    private void setupMock(final GoogleCaptchaSiteVerifyResponseDTO responseDTO) {
        when(this.googleRecaptchaClient.verify(anyString(), anyString(), anyString(), anyString())).thenReturn(responseDTO);
    }
}
