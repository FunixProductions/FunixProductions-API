package com.funixproductions.api.twitch.auth.service.resources;

import com.funixproductions.api.encryption.client.clients.FunixProductionsEncryptionClient;
import com.funixproductions.api.twitch.auth.client.enums.TwitchClientTokenType;
import com.funixproductions.api.twitch.auth.service.clients.TwitchTokenAuthClient;
import com.funixproductions.api.twitch.auth.service.clients.TwitchValidTokenClient;
import com.funixproductions.api.twitch.auth.service.dtos.TwitchTokenResponseDTO;
import com.funixproductions.api.twitch.auth.service.dtos.TwitchValidationTokenResponseDTO;
import com.funixproductions.api.user.client.clients.UserAuthClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.core.tools.string.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class TwitchAuthResourceTest {

    @MockBean
    private UserAuthClient userAuthClient;

    @MockBean
    private TwitchTokenAuthClient twitchTokenAuthClient;

    @MockBean
    private TwitchValidTokenClient twitchValidTokenClient;

    @MockBean
    private FunixProductionsEncryptionClient funixProductionsEncryptionClient;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setupFeignClients() {
        final PasswordGenerator passwordGenerator = new PasswordGenerator();
        passwordGenerator.setSpecialCharsAmount(0);

        final TwitchTokenResponseDTO mockToken = new TwitchTokenResponseDTO();
        mockToken.setAccessToken("access" + passwordGenerator.generateRandomPassword());
        mockToken.setRefreshToken("refersh" + passwordGenerator.generateRandomPassword());
        mockToken.setTokenType("bearer");
        mockToken.setExpiresIn(3000);

        final TwitchValidationTokenResponseDTO validResponseMock = new TwitchValidationTokenResponseDTO();
        validResponseMock.setTwitchUserId("lqksjdsldgh");
        validResponseMock.setTwitchUsername("funix");

        Mockito.when(twitchTokenAuthClient.getToken(Mockito.any())).thenReturn(mockToken);
        Mockito.when(twitchValidTokenClient.makeHttpRequestValidation(anyString())).thenReturn(validResponseMock);

        final UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setRole(UserRole.USER);
        userDTO.setEmail(UUID.randomUUID() + "email@test.fr");
        userDTO.setUsername(UUID.randomUUID() + "username");
        userDTO.setCreatedAt(new Date());

        Mockito.when(userAuthClient.current(Mockito.any())).thenReturn(userDTO);

        Mockito.when(this.funixProductionsEncryptionClient.decrypt(anyString())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(this.funixProductionsEncryptionClient.encrypt(anyString())).thenReturn(UUID.randomUUID().toString());
    }

    @Test
    void testGetAuthUrlSuccess() throws Exception {
        mockMvc.perform(get("/twitch/auth/clientAuthUrl"))
                .andExpect(status().isUnauthorized());

        MvcResult result = mockMvc.perform(get("/twitch/auth/clientAuthUrl")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                ).andExpect(status().isOk())
                .andReturn();
        String res = result.getResponse().getContentAsString();

        result = mockMvc.perform(get("/twitch/auth/clientAuthUrl?tokenType=" + TwitchClientTokenType.STREAMER.name())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                ).andExpect(status().isOk())
                .andReturn();
        String res2 = result.getResponse().getContentAsString();

        assertTrue(res2.length() > res.length());
        assertTrue(res.contains("client_id"));
        assertTrue(res.contains("redirect_uri"));
        assertTrue(res.contains("response_type"));
        assertTrue(res.contains("scope"));
        assertTrue(res.contains("state"));
    }

    @Test
    void testTwitchCallbackRoute() throws Exception {
        MvcResult result = mockMvc.perform(get("/twitch/auth/clientAuthUrl")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                ).andExpect(status().isOk())
                .andReturn();
        final String url = result.getResponse().getContentAsString();
        final String csrfCode = getCsrfCodeFromUrl(url);

        result = mockMvc.perform(get("/twitch/auth/cb" +
                        String.format("?code=sdfsdfsdfsdfsdqfthfyh&state=%s", csrfCode))
                ).andExpect(status().isOk())
                .andReturn();
        String responseCallback = result.getResponse().getContentAsString();
        assertTrue(responseCallback.contains("connecté"));

        mockMvc.perform(get("/twitch/auth/cb")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/twitch/auth/cb?code=dfggopaizepoaznqsdlb&state=codeAuPifNonValide")).andExpect(status().isBadRequest());

        result = mockMvc.perform(get("/twitch/auth/cb?error=error_custom&error_description=Une erreur custom faite exprès."))
                .andExpect(status().isOk())
                .andReturn();
        responseCallback = result.getResponse().getContentAsString();
        assertTrue(responseCallback.contains("erreur"));
    }

    @Test
    void testGetAccessTokenSuccess() throws Exception {
        mockMvc.perform(get("/twitch/auth/accessToken"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/twitch/auth/accessToken")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                ).andExpect(status().isNotFound());

        MvcResult result = mockMvc.perform(get("/twitch/auth/clientAuthUrl")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                ).andExpect(status().isOk())
                .andReturn();
        String url = result.getResponse().getContentAsString();
        String csrfCode = getCsrfCodeFromUrl(url);
        mockMvc.perform(get("/twitch/auth/cb" +
                String.format("?code=ioqapeiuycxnbveryjtgmlykhpodf&state=%s", csrfCode))
        ).andExpect(status().isOk());
        mockMvc.perform(get("/twitch/auth/accessToken")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
        ).andExpect(status().isOk());
    }

    @Test
    void testGetAccessTokenStreamerSuccess() throws Exception {
        mockMvc.perform(get("/twitch/auth/accessToken")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
        ).andExpect(status().isNotFound());
        MvcResult result = mockMvc.perform(get("/twitch/auth/clientAuthUrl?tokenType=" + TwitchClientTokenType.STREAMER.name())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                ).andExpect(status().isOk())
                .andReturn();
        String url = result.getResponse().getContentAsString();
        String csrfCode = getCsrfCodeFromUrl(url);
        mockMvc.perform(get("/twitch/auth/cb" +
                String.format("?code=piiuuiezuoioueizryuierzaovvjkdshhfjqdshgjkfdiu&state=%s", csrfCode))
        ).andExpect(status().isOk());
        mockMvc.perform(get("/twitch/auth/accessToken?tokenType=" + TwitchClientTokenType.STREAMER.name())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
        ).andExpect(status().isOk());
    }

    private String getCsrfCodeFromUrl(final String url) {
        final String toScan = "&state=";
        return url.substring(url.lastIndexOf(toScan) + toScan.length());
    }
}
