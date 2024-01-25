package com.funixproductions.api.payment.billing.service.resources;

import com.funixproductions.api.user.client.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BillingAdminResourceTest extends BillingResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllRightPermission() throws Exception {
        mockMvc.perform(get("/billing/admin"))
                .andExpect(status().isUnauthorized());

        setupAuth(UserRole.ADMIN);

        mockMvc.perform(get("/billing/admin")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID()))
                .andExpect(status().isOk());

        setupAuth(UserRole.USER);

        mockMvc.perform(get("/billing/admin")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
        ).andExpect(status().isForbidden());
    }

    @Test
    void testGetInvoiceByIdRightPermission() throws Exception {
        mockMvc.perform(get("/billing/admin/" + UUID.randomUUID()))
                .andExpect(status().isUnauthorized());

        setupAuth(UserRole.ADMIN);

        mockMvc.perform(get("/billing/admin/" + UUID.randomUUID())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID()))
                .andExpect(status().isOk());

        setupAuth(UserRole.USER);

        mockMvc.perform(get("/billing/admin/" + UUID.randomUUID())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDownloadInvoiceByIdRightPermission() throws Exception {
        mockMvc.perform(get("/billing/admin/" + UUID.randomUUID() + "/invoice"))
                .andExpect(status().isUnauthorized());

        setupAuth(UserRole.ADMIN);

        mockMvc.perform(get("/billing/admin/" + UUID.randomUUID() + "/invoice")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID()))
                .andExpect(status().isOk());

        setupAuth(UserRole.USER);

        mockMvc.perform(get("/billing/admin/" + UUID.randomUUID() + "/invoice")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID()))

                .andExpect(status().isForbidden());
    }

}
