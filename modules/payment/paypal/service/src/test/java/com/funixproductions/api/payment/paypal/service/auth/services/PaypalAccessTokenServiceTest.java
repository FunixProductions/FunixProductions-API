package com.funixproductions.api.payment.paypal.service.auth.services;

import com.funixproductions.api.payment.paypal.service.auth.clients.PaypalAuthClient;
import com.funixproductions.api.payment.paypal.service.auth.dtos.PaypalTokenAuth;
import com.funixproductions.core.exceptions.ApiException;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class PaypalAccessTokenServiceTest {

    @MockitoBean
    private PaypalAuthClient paypalAuthClient;

    @Autowired
    private PaypalAccessTokenService service;

    @BeforeEach
    void resetMocks() throws Exception {
        Mockito.reset(paypalAuthClient);

        final Field field = service.getClass().getDeclaredField("tokenAuth");
        field.setAccessible(true);
        field.set(service, null);
        field.setAccessible(false);
    }

    @Test
    void testGetAuthValid() {
        final PaypalTokenAuth tokenAuth = new PaypalTokenAuth();
        tokenAuth.setAccessToken(UUID.randomUUID().toString());
        tokenAuth.setAppId("appId");
        tokenAuth.setExpiresIn(1000);

        when(paypalAuthClient.getToken(anyString())).thenReturn(tokenAuth);
        service.refreshPaypalToken();
        assertEquals(tokenAuth.getAccessToken(), service.getAccessToken());
    }

    @Test
    void testGetAuthExpiredToken() {
        final PaypalTokenAuth tokenAuth = new PaypalTokenAuth();
        tokenAuth.setAccessToken(UUID.randomUUID().toString());
        tokenAuth.setAppId("appId");
        tokenAuth.setExpiresIn(1);

        when(paypalAuthClient.getToken(anyString())).thenReturn(tokenAuth);
        service.refreshPaypalToken();
        assertThrows(ApiException.class, service::getAccessToken);
    }

    @Test
    void testGetAuthWithFeignException() {
        final Request request = Request.create(
                Request.HttpMethod.GET,
                "url",
                new HashMap<>(),
                "test".getBytes(),
                StandardCharsets.UTF_8,
                new RequestTemplate()
        );

        final FeignException exception = new FeignException.InternalServerError(
                "Mock error",
                request,
                "Mock error body".getBytes(),
                new HashMap<>()
        );

        when(paypalAuthClient.getToken(anyString())).thenThrow(exception);
        service.refreshPaypalToken();
        assertThrows(ApiException.class, service::getAccessToken);
    }

}
