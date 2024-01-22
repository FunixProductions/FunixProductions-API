package com.funixproductions.api.payment.paypal.service.orders.resources;

import com.funixproductions.api.payment.paypal.client.dtos.requests.PaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.requests.card.CreditCardPaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.requests.paypal.PaypalPaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalOrderDTO;
import com.funixproductions.api.payment.paypal.client.enums.OrderStatus;
import com.funixproductions.api.payment.paypal.service.orders.dtos.PurchaseUnitDTO;
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

import java.util.ArrayList;
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
                UUID.randomUUID() + "@gmail.com",
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
        responseDTO.setPaymentSource(new PaypalOrderResponseDTO.PaymentSource(
                new PaypalOrderResponseDTO.PaymentSource.Card(
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        new ArrayList<>(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        new PaypalOrderResponseDTO.PaymentSource.Card.AuthenticationResult(
                                PaypalOrderResponseDTO.PaymentSource.Card.AuthenticationResult.LiabilityShift.NO,
                                new PaypalOrderResponseDTO.PaymentSource.Card.AuthenticationResult.ThreeDSecure(
                                        PaypalOrderResponseDTO.PaymentSource.Card.AuthenticationResult.ThreeDSecure.AuthenticationStatus.A,
                                        PaypalOrderResponseDTO.PaymentSource.Card.AuthenticationResult.ThreeDSecure.EnrollmentStatus.B
                                )
                        ),
                        new PaypalOrderResponseDTO.PaymentSource.Card.BinDetails(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString()
                        )
                ),
                null
        ));
        responseDTO.setStatus(OrderStatus.CREATED);

        final PaypalOrderResponseDTO.PurchaseUnitResponse purchaseUnitResponse = new PaypalOrderResponseDTO.PurchaseUnitResponse();
        purchaseUnitResponse.setAmount(new PurchaseUnitDTO.Amount(
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
        ));
        purchaseUnitResponse.setCustomId(UUID.randomUUID().toString());
        purchaseUnitResponse.setDescription(UUID.randomUUID().toString());
        purchaseUnitResponse.setItems(List.of(
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
        ));
        purchaseUnitResponse.setPayee(new PurchaseUnitDTO.Payee(
                UUID.randomUUID().toString(),
                null
        ));
        purchaseUnitResponse.setReferenceId(UUID.randomUUID().toString());
        purchaseUnitResponse.setSoftDescriptor(UUID.randomUUID().toString());
        responseDTO.setPurchaseUnits(List.of(purchaseUnitResponse));

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
                UUID.randomUUID() + "@gmail.com",
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
        responseDTO.setPaymentSource(new PaypalOrderResponseDTO.PaymentSource(
                null,
                new PaypalOrderResponseDTO.PaymentSource.PayPal(
                        PaypalOrderResponseDTO.PaymentSource.PayPal.AccountStatus.VERIFIED,
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        new PaypalOrderResponseDTO.PaymentSource.PayPal.Name(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString()
                        ),
                        UUID.randomUUID().toString()
                )
        ));
        responseDTO.setStatus(OrderStatus.CREATED);

        final PaypalOrderResponseDTO.PurchaseUnitResponse purchaseUnitResponse = new PaypalOrderResponseDTO.PurchaseUnitResponse();
        purchaseUnitResponse.setAmount(new PurchaseUnitDTO.Amount(
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
        ));
        purchaseUnitResponse.setCustomId(UUID.randomUUID().toString());
        purchaseUnitResponse.setDescription(UUID.randomUUID().toString());
        purchaseUnitResponse.setItems(List.of(
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
        ));
        purchaseUnitResponse.setPayee(new PurchaseUnitDTO.Payee(
                UUID.randomUUID().toString(),
                null
        ));
        purchaseUnitResponse.setReferenceId(UUID.randomUUID().toString());
        purchaseUnitResponse.setSoftDescriptor(UUID.randomUUID().toString());
        responseDTO.setPurchaseUnits(List.of(purchaseUnitResponse));

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
