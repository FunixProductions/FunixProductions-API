package com.funixproductions.api.payment.paypal.service.orders.services;

import com.funixproductions.api.payment.paypal.service.auth.services.PaypalAccessTokenService;
import com.funixproductions.api.payment.paypal.service.orders.clients.PaypalFeignOrderClient;
import com.funixproductions.api.payment.paypal.service.orders.dtos.requests.PaypalOrderCreationDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.responses.PaypalOrderResponseDTO;
import com.funixproductions.core.exceptions.ApiException;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class PaypalOrderServiceTest {

    @MockBean
    private PaypalFeignOrderClient orderClient;

    @MockBean
    private PaypalAccessTokenService tokenService;

    @Autowired
    private PaypalOrderService service;

    @BeforeEach
    void setupMocks() {
        reset(orderClient);
        when(tokenService.getAccessToken()).thenReturn("token");
        doNothing().when(tokenService).refreshPaypalToken();
    }

    @Test
    void testValidRoutes() throws ApiException {
        final PaypalOrderResponseDTO responseDTO = new PaypalOrderResponseDTO();

        when(orderClient.createOrder(anyString(), anyString(), any(PaypalOrderCreationDTO.class))).thenReturn(responseDTO);
        when(orderClient.getOrder(anyString())).thenReturn(responseDTO);
        when(orderClient.captureOrder(anyString(), anyString(), anyString(), anyString())).thenReturn(responseDTO);
        when(orderClient.authorizeOrder(anyString(), anyString(), anyString(), anyString())).thenReturn(responseDTO);

        assertNotNull(service.createOrder("meta", "id", new PaypalOrderCreationDTO()));
        assertNotNull(service.getOrder("orderId"));
        assertNotNull(service.authorizeOrder("authSession", "metaId", "requestId", "orderId"));
        assertNotNull(service.captureOrder("authSession", "metaId", "requestid", "orderId"));
    }

    @Test
    void testInvalidRoutes() {
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

        doThrow(exception).when(orderClient).createOrder(anyString(), anyString(), any(PaypalOrderCreationDTO.class));
        doThrow(exception).when(orderClient).getOrder(anyString());
        doThrow(exception).when(orderClient).captureOrder(anyString(), anyString(), anyString(), anyString());
        doThrow(exception).when(orderClient).authorizeOrder(anyString(), anyString(), anyString(), anyString());

        assertThrows(ApiException.class, () -> {
            service.createOrder("meta", "id", new PaypalOrderCreationDTO());
        });

        assertThrows(ApiException.class, () -> {
            service.getOrder("orderId");
        });

        assertThrows(ApiException.class, () -> {
            service.authorizeOrder("authSession", "metaId", "requestId", "orderId");
        });

        assertThrows(ApiException.class, () -> {
            service.captureOrder("authSession", "metaId", "requestid", "orderId");
        });

    }

}
