package com.funixproductions.api.encryption.service.ressources;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class FunixProductionEncryptionResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void encrypt() throws Exception {
        final String data = "test";

        final String res = mockMvc.perform(MockMvcRequestBuilders.post("/encryption/encrypt")
                .contentType("text/plain")
                .content(data))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertTrue(Strings.isNotBlank(res));
        assertNotEquals(data, res);
    }

    @Test
    void decrypt() throws Exception {
        final String data = "test";

        final String res1 = mockMvc.perform(MockMvcRequestBuilders.post("/encryption/encrypt")
                        .contentType("text/plain")
                        .content(data))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        final String res2 = mockMvc.perform(MockMvcRequestBuilders.post("/encryption/decrypt")
                .contentType("text/plain")
                .content(res1))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        assertEquals(data, res2);
    }

}
