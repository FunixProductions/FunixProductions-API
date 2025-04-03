package com.funixproductions.api.payment.paypal.service.subscriptions;

import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalPlanDTO;
import com.funixproductions.api.payment.paypal.service.subscriptions.clients.PaypalServicePlansClient;
import com.funixproductions.api.payment.paypal.service.subscriptions.clients.PaypalServiceProductsClient;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.requests.CreatePaypalPlanRequest;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.requests.CreatePaypalProductRequest;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses.PaypalPlanResponse;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses.PaypalProductResponse;
import com.funixproductions.api.payment.paypal.service.subscriptions.repositories.PaypalPlanRepository;
import com.funixproductions.core.crud.dtos.PageDTO;
import com.funixproductions.core.crud.enums.SearchOperation;
import com.funixproductions.core.test.beans.JsonHelper;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Type;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaypalPlanResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonHelper jsonHelper;

    @Autowired
    private PaypalPlanRepository paypalPlanRepository;

    @MockitoBean
    private PaypalServicePlansClient paypalServicePlansClient;

    @MockitoBean
    private PaypalServiceProductsClient paypalServiceProductsClient;

    @BeforeEach
    void resetDb() {
        this.paypalPlanRepository.deleteAll();
    }

    @Test
    void createNewPaypalPlan() throws Exception {
        final PaypalPlanDTO request = new PaypalPlanDTO(
                "TestSubscriptionPlan",
                "Test Subscription Plan",
                "https://www.test.com/image.jpg",
                "https://www.test.com",
                10.0,
                "testfunixproductions"
        );

        final PaypalPlanResponse paypalMockResponse = new PaypalPlanResponse(UUID.randomUUID().toString());
        when(this.paypalServicePlansClient.createPlan(anyString(), any(CreatePaypalPlanRequest.class))).thenReturn(paypalMockResponse);
        when(this.paypalServiceProductsClient.createProduct(anyString(), any(CreatePaypalProductRequest.class))).thenReturn(new PaypalProductResponse(UUID.randomUUID().toString()));

        MvcResult result = this.mockMvc.perform(
                post("/paypal/plans")
                        .content(this.jsonHelper.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        final PaypalPlanDTO response = this.jsonHelper.fromJson(result.getResponse().getContentAsString(), PaypalPlanDTO.class);

        assertNotNull(response);
        assertEquals(paypalMockResponse.getId(), response.getPlanId());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getDescription(), response.getDescription());
        assertEquals(request.getImageUrl(), response.getImageUrl());
        assertEquals(request.getHomeUrl(), response.getHomeUrl());
        assertEquals(request.getPrice(), response.getPrice());
        assertEquals(request.getProjectName(), response.getProjectName());
        assertNotEquals(request, response);
        assertNotEquals(request.hashCode(), response.hashCode());
    }

    @Test
    void createNewPaypalPlanWithNameAlreadyExistsShouldFail() throws Exception {
        when(this.paypalServicePlansClient.createPlan(anyString(), any(CreatePaypalPlanRequest.class))).thenReturn(new PaypalPlanResponse(UUID.randomUUID().toString()));
        when(this.paypalServiceProductsClient.createProduct(anyString(), any(CreatePaypalProductRequest.class))).thenReturn(new PaypalProductResponse(UUID.randomUUID().toString()));

        final PaypalPlanDTO request = new PaypalPlanDTO(
                "TestSubscriptionPlan",
                "Test Subscription Plan",
                "https://www.test.com/image.jpg",
                "https://www.test.com",
                10.0,
                "testfunixproductions"
        );

        this.mockMvc.perform(
                post("/paypal/plans")
                        .content(this.jsonHelper.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        this.mockMvc.perform(
                post("/paypal/plans")
                        .content(this.jsonHelper.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        when(this.paypalServicePlansClient.createPlan(anyString(), any(CreatePaypalPlanRequest.class))).thenReturn(new PaypalPlanResponse(UUID.randomUUID().toString()));
        when(this.paypalServiceProductsClient.createProduct(anyString(), any(CreatePaypalProductRequest.class))).thenReturn(new PaypalProductResponse(UUID.randomUUID().toString()));

        request.setProjectName("testfunixproductions2");
        this.mockMvc.perform(
                post("/paypal/plans")
                        .content(this.jsonHelper.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        when(this.paypalServicePlansClient.createPlan(anyString(), any(CreatePaypalPlanRequest.class))).thenReturn(new PaypalPlanResponse(UUID.randomUUID().toString()));
        when(this.paypalServiceProductsClient.createProduct(anyString(), any(CreatePaypalProductRequest.class))).thenReturn(new PaypalProductResponse(UUID.randomUUID().toString()));

        request.setProjectName("testfunixproductions");
        request.setName("dd");
        this.mockMvc.perform(
                post("/paypal/plans")
                        .content(this.jsonHelper.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void testGetPlanById() throws Exception {
        when(this.paypalServicePlansClient.createPlan(anyString(), any(CreatePaypalPlanRequest.class))).thenReturn(new PaypalPlanResponse(UUID.randomUUID().toString()));
        when(this.paypalServiceProductsClient.createProduct(anyString(), any(CreatePaypalProductRequest.class))).thenReturn(new PaypalProductResponse(UUID.randomUUID().toString()));

        MvcResult result = this.mockMvc.perform(
                post("/paypal/plans")
                        .content(this.jsonHelper.toJson(new PaypalPlanDTO(
                                "TestSubscriptionPlan",
                                "Test Subscription Plan",
                                "https://www.test.com/image.jpg",
                                "https://www.test.com",
                                10.0,
                                "testfunixproductions"
                        )))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        final PaypalPlanDTO response = this.jsonHelper.fromJson(result.getResponse().getContentAsString(), PaypalPlanDTO.class);

        this.mockMvc.perform(
                get("/paypal/plans/" + response.getPlanId())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());

        this.mockMvc.perform(
                get("/paypal/plans/" + UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());

        result = this.mockMvc.perform(
                get("/paypal/plans/" + response.getId())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        final PaypalPlanDTO responseFromGet = this.jsonHelper.fromJson(result.getResponse().getContentAsString(), PaypalPlanDTO.class);

        assertEquals(response, responseFromGet);
    }

    @Test
    void testGetPlansBySearch() throws Exception {
        when(this.paypalServicePlansClient.createPlan(anyString(), any(CreatePaypalPlanRequest.class))).thenReturn(new PaypalPlanResponse(UUID.randomUUID().toString()));
        when(this.paypalServiceProductsClient.createProduct(anyString(), any(CreatePaypalProductRequest.class))).thenReturn(new PaypalProductResponse(UUID.randomUUID().toString()));

        this.mockMvc.perform(
                post("/paypal/plans")
                        .content(this.jsonHelper.toJson(new PaypalPlanDTO(
                                "TestSubscriptionPlan",
                                "Test Subscription Plan",
                                "https://www.test.com/image.jpg",
                                "https://www.test.com",
                                10.0,
                                "testfunixproductions"
                        )))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        when(this.paypalServicePlansClient.createPlan(anyString(), any(CreatePaypalPlanRequest.class))).thenReturn(new PaypalPlanResponse(UUID.randomUUID().toString()));
        when(this.paypalServiceProductsClient.createProduct(anyString(), any(CreatePaypalProductRequest.class))).thenReturn(new PaypalProductResponse(UUID.randomUUID().toString()));

        this.mockMvc.perform(
                post("/paypal/plans")
                        .content(this.jsonHelper.toJson(new PaypalPlanDTO(
                                "TestSubscriptionPlan2",
                                "Test Subscription Plan",
                                "https://www.test.com/image.jpg",
                                "https://www.test.com",
                                10.0,
                                "testfunixproductions"
                        )))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        when(this.paypalServicePlansClient.createPlan(anyString(), any(CreatePaypalPlanRequest.class))).thenReturn(new PaypalPlanResponse(UUID.randomUUID().toString()));
        when(this.paypalServiceProductsClient.createProduct(anyString(), any(CreatePaypalProductRequest.class))).thenReturn(new PaypalProductResponse(UUID.randomUUID().toString()));

        this.mockMvc.perform(
                post("/paypal/plans")
                        .content(this.jsonHelper.toJson(new PaypalPlanDTO(
                                "TestSubscriptionPlan",
                                "Test Subscription Plan",
                                "https://www.test.com/image.jpg",
                                "https://www.test.com",
                                10.0,
                                "testpacifista"
                        )))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        PageDTO<PaypalPlanDTO> page = this.getSearch(null, null);
        assertEquals(3, page.getContent().size());

        page = this.getSearch("TestSubscriptionPlan", null);
        assertEquals(2, page.getContent().size());

        page = this.getSearch(null, "testfunixproductions");
        assertEquals(2, page.getContent().size());

        page = this.getSearch("TestSubscriptionPlan", "testfunixproductions");
        assertEquals(1, page.getContent().size());

        page = this.getSearch("TestSubscriptionPlan", "testpacifista");
        assertEquals(1, page.getContent().size());

        page = this.getSearch("TestSubscriptionPlan2", "testpacifista");
        assertEquals(0, page.getContent().size());

        page = this.getSearch("TestSubscriptionPlan2", "testfunixproductions");
        assertEquals(1, page.getContent().size());
    }

    private PageDTO<PaypalPlanDTO> getSearch(@Nullable final String planName, @Nullable final String projectName) throws Exception {
        final String query;

        if (!Strings.isEmpty(planName) && !Strings.isEmpty(projectName)) {
            query = String.format(
                    "name:%s:%s,projectName:%s:%s",
                    SearchOperation.EQUALS.getOperation(),
                    planName,
                    SearchOperation.EQUALS.getOperation(),
                    projectName
            );
        } else if (!Strings.isEmpty(planName)) {
            query = String.format(
                    "name:%s:%s",
                    SearchOperation.EQUALS.getOperation(),
                    planName
            );
        } else if (!Strings.isEmpty(projectName)) {
            query = String.format(
                    "projectName:%s:%s",
                    SearchOperation.EQUALS.getOperation(),
                    projectName
            );
        } else {
            query = "";
        }

        final MvcResult mvcResult = this.mockMvc.perform(
                get("/paypal/plans")
                    .param("search", query)
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        final Type pageType = new TypeToken<PageDTO<PaypalPlanDTO>>() {}.getType();
        return this.jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), pageType);
    }

}
