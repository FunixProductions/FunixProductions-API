package com.funixproductions.api.payment.paypal.service.subscriptions;

import com.funixproductions.api.payment.paypal.client.dtos.requests.paypal.PaypalCreateSubscriptionDTO;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalPlanDTO;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalSubscriptionDTO;
import com.funixproductions.api.payment.paypal.service.subscriptions.clients.PaypalServicePlansClient;
import com.funixproductions.api.payment.paypal.service.subscriptions.clients.PaypalServiceProductsClient;
import com.funixproductions.api.payment.paypal.service.subscriptions.clients.PaypalServiceSubscriptionsClient;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.requests.CreatePaypalPlanRequest;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.requests.CreatePaypalProductRequest;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.requests.CreatePaypalSubscriptionRequest;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses.PaypalPlanResponse;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses.PaypalProductResponse;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses.PaypalSubscriptionResponse;
import com.funixproductions.api.payment.paypal.service.subscriptions.repositories.PaypalPlanRepository;
import com.funixproductions.api.payment.paypal.service.subscriptions.repositories.PaypalSubscriptionRepository;
import com.funixproductions.api.user.client.clients.InternalUserCrudClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.core.test.beans.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaypalSubscriptionResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonHelper jsonHelper;

    @Autowired
    private PaypalSubscriptionRepository subscriptionRepository;

    @Autowired
    private PaypalPlanRepository paypalPlanRepository;

    @MockitoBean
    private PaypalServiceSubscriptionsClient paypalServiceSubscriptionsClient;

    @MockitoBean
    private InternalUserCrudClient internalUserCrudClient;

    @BeforeEach
    void cleanDb() {
        this.subscriptionRepository.deleteAll();
        this.paypalPlanRepository.deleteAll();
    }

    @Test
    void createSubscription() throws Exception {
        final UserDTO user = this.createUserDTO(UUID.randomUUID());
        final PaypalPlanDTO paypalPlanDTO = this.createPlanDTO();
        final PaypalCreateSubscriptionDTO request = new PaypalCreateSubscriptionDTO(
                paypalPlanDTO,
                user.getId(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "testfunixprod"
        );

        generateFakeSubscriptionPaypal(paypalPlanDTO);

        MvcResult result = this.mockMvc.perform(
                post("/paypal/subscriptions")
                        .content(this.jsonHelper.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        final PaypalSubscriptionDTO response = this.jsonHelper.fromJson(result.getResponse().getContentAsString(), PaypalSubscriptionDTO.class);

        assertNotNull(response);
        assertNotNull(response.getPlan());
        assertNotNull(response.getPlan().getPlanId());
        assertNotNull(response.getSubscriptionId());
        assertEquals(user.getId(), response.getFunixProdUserId());
        assertTrue(response.getActive());
        assertEquals(1, response.getCyclesCompleted());
        assertNotNull(response.getLastPaymentDate());
        assertNotNull(response.getNextPaymentDate());
        assertNotNull(response.getApproveLink());
    }

    @Test
    void testGetSubscriptionById() throws Exception {
        final PaypalSubscriptionDTO responseCreate = this.createSub();

        MvcResult result = this.mockMvc.perform(
                get("/paypal/subscriptions/" + responseCreate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        final PaypalSubscriptionDTO response = this.jsonHelper.fromJson(result.getResponse().getContentAsString(), PaypalSubscriptionDTO.class);

        assertNotNull(response);
        assertNotNull(response.getPlan());
        assertNotNull(response.getPlan().getPlanId());
        assertNotNull(response.getSubscriptionId());
        assertTrue(response.getActive());
        assertEquals(1, response.getCyclesCompleted());
        assertNotNull(response.getLastPaymentDate());
        assertNotNull(response.getNextPaymentDate());
        assertNotNull(response.getApproveLink());
    }

    @Test
    void testPauseSubscription() throws Exception {
        final PaypalSubscriptionDTO responseCreate = this.createSub();

        generateFakePauseSubscriptionPaypal(responseCreate);

        this.mockMvc.perform(
                post("/paypal/subscriptions/" + responseCreate.getId() + "/pause")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void testActivateSubscription() throws Exception {
        final PaypalSubscriptionDTO responseCreate = this.createSub();

        generateFakeActivateSubscriptionPaypal(responseCreate);

        this.mockMvc.perform(
                post("/paypal/subscriptions/" + responseCreate.getId() + "/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void testCancelSubscription() throws Exception {
        final PaypalSubscriptionDTO responseCreate = this.createSub();

        generateFakeCancelSubscriptionPaypal(responseCreate);

        this.mockMvc.perform(
                post("/paypal/subscriptions/" + responseCreate.getId() + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void testGetAllSubs() throws Exception {
        this.createSub();

        this.mockMvc.perform(
                get("/paypal/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @MockitoBean
    private PaypalServicePlansClient paypalServicePlansClient;

    @MockitoBean
    private PaypalServiceProductsClient paypalServiceProductsClient;

    private PaypalPlanDTO createPlanDTO() throws Exception {
        final Random random = new Random();

        final PaypalPlanDTO paypalPlanDTO = new PaypalPlanDTO(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                random.nextDouble(1000) + 1,
                UUID.randomUUID().toString()
        );

        final PaypalPlanResponse paypalMockResponse = new PaypalPlanResponse(UUID.randomUUID().toString());
        when(this.paypalServicePlansClient.createPlan(anyString(), any(CreatePaypalPlanRequest.class))).thenReturn(paypalMockResponse);
        when(this.paypalServiceProductsClient.createProduct(anyString(), any(CreatePaypalProductRequest.class))).thenReturn(new PaypalProductResponse(UUID.randomUUID().toString()));

        MvcResult result = this.mockMvc.perform(
                post("/paypal/plans")
                        .content(this.jsonHelper.toJson(paypalPlanDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        return this.jsonHelper.fromJson(result.getResponse().getContentAsString(), PaypalPlanDTO.class);
    }

    private UserDTO createUserDTO(UUID userId) {
        final UserDTO userDTO = UserDTO.generateFakeDataForTestingPurposes();
        userDTO.setId(userId);

        when(this.internalUserCrudClient.findById(userId.toString())).thenReturn(userDTO);
        return userDTO;
    }

    private void generateFakeSubscriptionPaypal(PaypalPlanDTO paypalPlanDTO) {
        final PaypalSubscriptionResponse subscriptionResponse = new PaypalSubscriptionResponse(
                UUID.randomUUID().toString(),
                paypalPlanDTO.getPlanId(),
                "ACTIVE",
                new PaypalSubscriptionResponse.BillingInfo(
                        List.of(
                                new PaypalSubscriptionResponse.BillingInfo.CycleExecution(
                                        1
                                )
                        ),
                        new PaypalSubscriptionResponse.BillingInfo.LastPayment(
                                "COMPLETED",
                                Instant.now().toString()
                        ),
                        Instant.now().plus(30, ChronoUnit.DAYS).toString()
                ),
                List.of(
                        new PaypalSubscriptionResponse.Link(
                                UUID.randomUUID().toString(),
                                "approve",
                                "GET"
                        )
                )
        );

        when(this.paypalServiceSubscriptionsClient.createSubscription(anyString(), any(CreatePaypalSubscriptionRequest.class))).thenReturn(subscriptionResponse);
        when(this.paypalServiceSubscriptionsClient.getSubscription(anyString())).thenReturn(subscriptionResponse);
    }

    private void generateFakePauseSubscriptionPaypal(final PaypalSubscriptionDTO subscriptionDTO) {
        doNothing().when(this.paypalServiceSubscriptionsClient).pauseSubscription(eq(subscriptionDTO.getSubscriptionId()), anyString());
    }

    private void generateFakeActivateSubscriptionPaypal(final PaypalSubscriptionDTO subscriptionDTO) {
        doNothing().when(this.paypalServiceSubscriptionsClient).activateSubscription(eq(subscriptionDTO.getSubscriptionId()));
    }

    private void generateFakeCancelSubscriptionPaypal(final PaypalSubscriptionDTO subscriptionDTO) {
        doNothing().when(this.paypalServiceSubscriptionsClient).cancelSubscription(eq(subscriptionDTO.getSubscriptionId()), anyString());
    }

    private PaypalSubscriptionDTO createSub() throws Exception {
        final UserDTO user = this.createUserDTO(UUID.randomUUID());
        final PaypalPlanDTO paypalPlanDTO = this.createPlanDTO();
        final PaypalCreateSubscriptionDTO request = new PaypalCreateSubscriptionDTO(
                paypalPlanDTO,
                user.getId(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "testfunixprod"
        );

        generateFakeSubscriptionPaypal(paypalPlanDTO);

        MvcResult result = this.mockMvc.perform(
                post("/paypal/subscriptions")
                        .content(this.jsonHelper.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        return this.jsonHelper.fromJson(result.getResponse().getContentAsString(), PaypalSubscriptionDTO.class);
    }

}
