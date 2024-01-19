package com.funixproductions.api.payment.paypal.service.orders.resources;

import com.funixproductions.api.payment.paypal.client.dtos.requests.PaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.requests.card.CreditCardPaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.requests.paypal.PaypalPaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalOrderDTO;
import com.funixproductions.api.payment.paypal.client.enums.OrderStatus;
import com.funixproductions.api.payment.paypal.service.orders.dtos.PurchaseUnitDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.requests.PaypalOrderCreationDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.responses.PaypalOrderResponseDTO;
import com.funixproductions.api.payment.paypal.service.orders.services.PaypalOrderService;
import com.funixproductions.core.test.beans.JsonHelper;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaypalOrderResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaypalOrderService service;

    @Autowired
    private JsonHelper jsonHelper;

    @Test
    void testCreditCardPayment() throws Exception {
        final CreditCardPaymentDTO dto = new CreditCardPaymentDTO();

        dto.setCardHolderName(UUID.randomUUID().toString());
        dto.setCardNumber("1234567890123456");
        dto.setSecurityCode("123");
        dto.setExpirationMonth(10);
        dto.setExpirationYear(2025);
        dto.setCancelUrl("http://localhost:8080/cancel");
        dto.setReturnUrl("http://localhost:8080/return");
        dto.setUser(new CreditCardPaymentDTO.UserPaymentDTO(
                UUID.randomUUID(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        ));
        dto.setVatInformation(VATInformation.FRANCE);
        dto.setOriginRequest("Le test unitaire de fou");
        dto.setBillingAddress(new CreditCardPaymentDTO.BillingAddressDTO(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "FR"
        ));
        dto.setPurchaseUnits(List.of(
                new PaymentDTO.PurchaseUnitDTO(
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        List.of(
                                new PaymentDTO.PurchaseUnitDTO.Item(
                                        UUID.randomUUID().toString(),
                                        10,
                                        UUID.randomUUID().toString(),
                                        10.0,
                                        0.0
                                )
                        )
                )
        ));

        final PaypalOrderResponseDTO responseDTO = new PaypalOrderResponseDTO();
        responseDTO.setId(UUID.randomUUID().toString());
        responseDTO.setCreateTime("2021-10-10T10:10:10Z");
        responseDTO.setUpdateTime("2021-10-10T10:10:10Z");
        responseDTO.setLinks(List.of(
                new PaypalOrderResponseDTO.Link(
                        "http://localhost:8080/redirect",
                        "approve",
                        "GET"
                ),
                new PaypalOrderResponseDTO.Link(
                        "http://localhost:8080/cancel",
                        "cancel",
                        "GET"
                )
        ));
        responseDTO.setPaymentSource(new PaypalOrderCreationDTO.PaymentSource(
                null,
                new PaypalOrderCreationDTO.PaymentSource.Card(
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        null,
                        null,
                        null,
                        null
                )
        ));
        responseDTO.setStatus(OrderStatus.CREATED);
        responseDTO.setPurchaseUnits(List.of(
                new PurchaseUnitDTO(
                        new PurchaseUnitDTO.Amount(
                                "EUR",
                                "100.0",
                                new PurchaseUnitDTO.Amount.Breakdown(
                                        new PurchaseUnitDTO.Money(
                                                "EUR",
                                                "100.0"
                                        ),
                                        new PurchaseUnitDTO.Money(
                                                "EUR",
                                                "0.0"
                                        ),
                                        null
                                )
                        ),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        List.of(
                                new PurchaseUnitDTO.Item(
                                        UUID.randomUUID().toString(),
                                        "10",
                                        UUID.randomUUID().toString(),
                                        new PurchaseUnitDTO.Money(
                                                "EUR",
                                                "10.0"
                                        ),
                                        new PurchaseUnitDTO.Money(
                                                "EUR",
                                                "0.0"
                                        ),
                                        PurchaseUnitDTO.Category.DIGITAL_GOODS
                                )
                        ),
                        new PurchaseUnitDTO.Payee(
                                UUID.randomUUID().toString(),
                                null
                        ),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString()
                )
        ));

        when(this.service.createOrder(any(), any())).thenReturn(responseDTO);
        MvcResult mvcResult = mockMvc.perform(post("/paypal/orders/card")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonHelper.toJson(dto)))
                .andExpect(status().isOk())
                .andReturn();

        PaypalOrderDTO paypalOrderDTO = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), PaypalOrderDTO.class);
        assertNotNull(paypalOrderDTO.getUrlClientRedirection());
        assertNotNull(paypalOrderDTO.getOrderId());

        when(this.service.getOrder(any())).thenReturn(responseDTO);

        mockMvc.perform(get("/paypal/orders/" + paypalOrderDTO.getOrderId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/paypal/orders/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());

        responseDTO.setStatus(OrderStatus.COMPLETED);
        when(this.service.captureOrder(any(), any())).thenReturn(responseDTO);

        mockMvc.perform(post("/paypal/orders/" + UUID.randomUUID() + "/capture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(dto)))
                .andExpect(status().isNotFound());

        mvcResult = mockMvc.perform(post("/paypal/orders/" + paypalOrderDTO.getOrderId() + "/capture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(dto)))
                .andExpect(status().isOk())
                .andReturn();

        paypalOrderDTO = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), PaypalOrderDTO.class);
        assertEquals(OrderStatus.COMPLETED, paypalOrderDTO.getStatus());
        assertTrue(paypalOrderDTO.getCreditCardPayment());
    }

    @Test
    void testPaypalPayment() throws Exception {
        final PaypalPaymentDTO dto = new PaypalPaymentDTO();

        dto.setBrandName(UUID.randomUUID().toString());
        dto.setCancelUrl("http://localhost:8080/cancel");
        dto.setReturnUrl("http://localhost:8080/return");
        dto.setUser(new PaypalPaymentDTO.UserPaymentDTO(
                UUID.randomUUID(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        ));
        dto.setVatInformation(VATInformation.FRANCE);
        dto.setOriginRequest("Le test unitaire de fou");
        dto.setBillingAddress(new PaypalPaymentDTO.BillingAddressDTO(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "FR"
        ));
        dto.setPurchaseUnits(List.of(
                new PaymentDTO.PurchaseUnitDTO(
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        List.of(
                                new PaymentDTO.PurchaseUnitDTO.Item(
                                        UUID.randomUUID().toString(),
                                        10,
                                        UUID.randomUUID().toString(),
                                        10.0,
                                        0.0
                                )
                        )
                )
        ));

        final PaypalOrderResponseDTO responseDTO = new PaypalOrderResponseDTO();
        responseDTO.setId(UUID.randomUUID().toString());
        responseDTO.setCreateTime("2021-10-10T10:10:10Z");
        responseDTO.setUpdateTime("2021-10-10T10:10:10Z");
        responseDTO.setLinks(List.of(
                new PaypalOrderResponseDTO.Link(
                        "http://localhost:8080/redirect",
                        "approve",
                        "GET"
                ),
                new PaypalOrderResponseDTO.Link(
                        "http://localhost:8080/cancel",
                        "cancel",
                        "GET"
                )
        ));
        responseDTO.setPaymentSource(new PaypalOrderCreationDTO.PaymentSource(
                new PaypalOrderCreationDTO.PaymentSource.Paypal(
                        new PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.LandingPage.LOGIN,
                                "FR",
                                new PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.PaymentMethod(
                                        PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.PaymentMethod.PayeePreferred.IMMEDIATE_PAYMENT_REQUIRED,
                                        PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.PaymentMethod.StandardEntryClassCode.WEB
                                ),
                                PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.ShippingPreference.NO_SHIPPING,
                                new PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.StoredPaymentSource(
                                        PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.StoredPaymentSource.PaymentInitiator.CUSTOMER,
                                        PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.StoredPaymentSource.PaymentType.ONE_TIME,
                                        PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.StoredPaymentSource.Usage.FIRST
                                ),
                                PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.UserAction.PAY_NOW
                        ),
                        new PaypalOrderCreationDTO.PaymentSource.Paypal.Address(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                "FR"
                        )
                ),
                null
        ));
        responseDTO.setStatus(OrderStatus.CREATED);
        responseDTO.setPurchaseUnits(List.of(
                new PurchaseUnitDTO(
                        new PurchaseUnitDTO.Amount(
                                "EUR",
                                "100.0",
                                new PurchaseUnitDTO.Amount.Breakdown(
                                        new PurchaseUnitDTO.Money(
                                                "EUR",
                                                "100.0"
                                        ),
                                        new PurchaseUnitDTO.Money(
                                                "EUR",
                                                "0.0"
                                        ),
                                        null
                                )
                        ),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        List.of(
                                new PurchaseUnitDTO.Item(
                                        UUID.randomUUID().toString(),
                                        "10",
                                        UUID.randomUUID().toString(),
                                        new PurchaseUnitDTO.Money(
                                                "EUR",
                                                "10.0"
                                        ),
                                        new PurchaseUnitDTO.Money(
                                                "EUR",
                                                "0.0"
                                        ),
                                        PurchaseUnitDTO.Category.DIGITAL_GOODS
                                )
                        ),
                        new PurchaseUnitDTO.Payee(
                                UUID.randomUUID().toString(),
                                null
                        ),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString()
                )
        ));

        when(this.service.createOrder(any(), any())).thenReturn(responseDTO);
        MvcResult mvcResult = mockMvc.perform(post("/paypal/orders/paypal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(dto)))
                .andExpect(status().isOk())
                .andReturn();

        PaypalOrderDTO paypalOrderDTO = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), PaypalOrderDTO.class);
        assertNotNull(paypalOrderDTO.getUrlClientRedirection());
        assertNotNull(paypalOrderDTO.getOrderId());

        when(this.service.getOrder(any())).thenReturn(responseDTO);

        mockMvc.perform(get("/paypal/orders/" + paypalOrderDTO.getOrderId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/paypal/orders/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());

        responseDTO.setStatus(OrderStatus.COMPLETED);
        when(this.service.captureOrder(any(), any())).thenReturn(responseDTO);

        mockMvc.perform(post("/paypal/orders/" + UUID.randomUUID() + "/capture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(dto)))
                .andExpect(status().isNotFound());

        mvcResult = mockMvc.perform(post("/paypal/orders/" + paypalOrderDTO.getOrderId() + "/capture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(dto)))
                .andExpect(status().isOk())
                .andReturn();

        paypalOrderDTO = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), PaypalOrderDTO.class);
        assertEquals(OrderStatus.COMPLETED, paypalOrderDTO.getStatus());
        assertFalse(paypalOrderDTO.getCreditCardPayment());
    }

}
