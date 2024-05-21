package com.funixproductions.api.google.auth.service.resources;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InternalGoogleAuthResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDeleteAllByUserUuidIn() throws Exception {
        this.mockMvc.perform(delete("/kubeinternal/google/auth?ids=" + UUID.randomUUID()))
            .andExpect(status().isOk());
    }

}
