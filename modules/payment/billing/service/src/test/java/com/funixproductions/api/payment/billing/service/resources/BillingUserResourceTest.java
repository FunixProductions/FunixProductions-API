package com.funixproductions.api.payment.billing.service.resources;

import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BillingUserResourceTest extends BillingResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllRightPermission() throws Exception {
        mockMvc.perform(get("/billing/user"))
                .andExpect(status().isUnauthorized());

        setupAuth(UserRole.ADMIN);

        mockMvc.perform(get("/billing/user")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID()))
                .andExpect(status().isOk());

        setupAuth(UserRole.USER);

        mockMvc.perform(get("/billing/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
        ).andExpect(status().isOk());
    }

    @Test
    void testGetInvoiceByIdRightPermission() throws Exception {
        reset(userAuthClient);

        final UserDTO userDto = new UserDTO();
        userDto.setId(UUID.fromString(billingDTO.getBilledEntity().getUserFunixProdId()));
        userDto.setUsername("test-EDIT-" + UUID.randomUUID());
        userDto.setEmail("test-" + UUID.randomUUID() + "@test.com");
        userDto.setRole(UserRole.USER);
        userDto.setCreatedAt(new Date());
        userDto.setValid(true);

        when(userAuthClient.current(any())).thenReturn(userDto);

        mockMvc.perform(get("/billing/user/" + billingDTO.getId()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/billing/user/" + billingDTO.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    void testDownloadInvoiceByIdRightPermission() throws Exception {
        mockMvc.perform(get("/billing/user/" + UUID.randomUUID() + "/invoice"))
                .andExpect(status().isUnauthorized());

        setupAuth(UserRole.ADMIN);

        mockMvc.perform(get("/billing/user/" + UUID.randomUUID() + "/invoice")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID()))
                .andExpect(status().isOk());

        setupAuth(UserRole.USER);

        mockMvc.perform(get("/billing/user/" + UUID.randomUUID() + "/invoice")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/billing/user/" + UUID.randomUUID() + "/invoice")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/billing/user/" + UUID.randomUUID() + "/invoice")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID()))
                .andExpect(status().isOk());
    }

}
