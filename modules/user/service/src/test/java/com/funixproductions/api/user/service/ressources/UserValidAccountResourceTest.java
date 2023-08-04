package com.funixproductions.api.user.service.ressources;

import com.funixproductions.api.encryption.client.clients.FunixProductionsEncryptionClient;
import com.funixproductions.api.google.gmail.client.clients.GoogleGmailClient;
import com.funixproductions.api.google.recaptcha.client.clients.GoogleRecaptchaInternalClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.client.dtos.requests.UserCreationDTO;
import com.funixproductions.api.user.service.components.UserTestComponent;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.entities.UserValidAccountToken;
import com.funixproductions.api.user.service.repositories.UserRepository;
import com.funixproductions.api.user.service.repositories.UserValidAccountTokenRepository;
import com.funixproductions.core.test.beans.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserValidAccountResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserTestComponent userTestComponent;

    @Autowired
    private UserValidAccountTokenRepository userValidAccountTokenRepository;

    @Autowired
    private JsonHelper jsonHelper;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private GoogleGmailClient gmailClient;

    @MockBean
    private GoogleRecaptchaInternalClient googleRecaptchaInternalClient;

    @MockBean
    private FunixProductionsEncryptionClient funixProductionsEncryptionClient;

    @BeforeEach
    void setupMocks() {
        doNothing().when(gmailClient).sendMail(any(), any());
        when(funixProductionsEncryptionClient.encrypt(any())).thenReturn("encryptedText" + UUID.randomUUID());
        when(funixProductionsEncryptionClient.decrypt(any())).thenReturn("decryptedText" + UUID.randomUUID());
        Mockito.doNothing().when(gmailClient).sendMail(any(), any());
        Mockito.doNothing().when(googleRecaptchaInternalClient).verify(any(), any(), any());
    }

    @Test
    void testSendRequestValidationEmail() throws Exception {
        final User user = userTestComponent.createBasicUser();
        final UserTokenDTO token = userTestComponent.loginUser(user);

        mockMvc.perform(post("/user/auth/valid-account")
                        .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(status().isOk());

        mockMvc.perform(post("/user/auth/valid-account")).andExpect(status().isUnauthorized());

        final UserValidAccountToken userValidAccountToken = userValidAccountTokenRepository.findByUser(user).get();

        mockMvc.perform(get("/user/auth/valid-account?token=" + userValidAccountToken.getValidationToken())).andExpect(status().isOk());

        final UserDTO userDTO = jsonHelper.fromJson(mockMvc.perform(get("/user/auth/current")
                        .header("Authorization", "Bearer " + token.getToken()))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), UserDTO.class);

        assertTrue(userDTO.getValid());
    }

    @Test
    void testSendRequestValidationEmailWithInvalidToken() throws Exception {
        mockMvc.perform(get("/user/auth/valid-account?token=invalidToken")).andExpect(status().isBadRequest());
    }

    @Test
    void testSendRequestOfAccountAlreadyValidated() throws Exception {
        final User user = userTestComponent.createBasicUser();
        final UserTokenDTO token = userTestComponent.loginUser(user);

        mockMvc.perform(post("/user/auth/valid-account")
                        .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(status().isOk());

        assertTrue(userValidAccountTokenRepository.findByUser(user).isPresent());
        final UserValidAccountToken userValidAccountToken = userValidAccountTokenRepository.findByUser(user).get();
        mockMvc.perform(get("/user/auth/valid-account?token=" + userValidAccountToken.getValidationToken())).andExpect(status().isOk());

        mockMvc.perform(post("/user/auth/valid-account")
                        .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterAndSendValidEmail() throws Exception {
        final UserCreationDTO creationDTO = new UserCreationDTO();
        creationDTO.setEmail(UUID.randomUUID() + "@gmail.com");
        creationDTO.setUsername(UUID.randomUUID().toString());
        creationDTO.setPassword("ousddffdi22AA");
        creationDTO.setPasswordConfirmation("ousddffdi22AA");
        creationDTO.setAcceptCGU(true);
        creationDTO.setAcceptCGV(true);

        when(funixProductionsEncryptionClient.encrypt(creationDTO.getPassword())).thenReturn(creationDTO.getPassword());
        when(funixProductionsEncryptionClient.decrypt(creationDTO.getPassword())).thenReturn(creationDTO.getPassword());

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(creationDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final UserDTO userDTO = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        assertFalse(userDTO.getValid());
        assertTrue(userRepository.findByUuid(userDTO.getId().toString()).isPresent());
        final User user = userRepository.findByUuid(userDTO.getId().toString()).get();

        assertTrue(userValidAccountTokenRepository.findByUser(user).isPresent());
        final UserValidAccountToken userValidAccountToken = userValidAccountTokenRepository.findByUser(user).get();

        mockMvc.perform(get("/user/auth/valid-account?token=" + userValidAccountToken.getValidationToken())).andExpect(status().isOk());

        final User userAfter = userRepository.findByUuid(userDTO.getId().toString()).get();
        assertTrue(userAfter.getValid());
    }

}
