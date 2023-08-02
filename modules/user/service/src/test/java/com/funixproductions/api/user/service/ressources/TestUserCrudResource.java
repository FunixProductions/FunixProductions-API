package com.funixproductions.api.user.service.ressources;

import com.funixproductions.api.encryption.client.clients.FunixProductionsEncryptionClient;
import com.funixproductions.api.google.auth.client.clients.InternalGoogleAuthClient;
import com.funixproductions.api.google.recaptcha.client.services.GoogleRecaptchaHandler;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.client.dtos.requests.UserSecretsDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.api.user.service.components.UserTestComponent;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.entities.UserToken;
import com.funixproductions.api.user.service.repositories.UserRepository;
import com.funixproductions.api.user.service.repositories.UserTokenRepository;
import com.funixproductions.core.test.beans.JsonHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyList;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class TestUserCrudResource {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonHelper jsonHelper;
    @Autowired
    private UserTestComponent userTestComponent;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;

    @MockBean
    private GoogleRecaptchaHandler googleCaptchaService;

    @MockBean
    private FunixProductionsEncryptionClient funixProductionsEncryptionClient;

    @MockBean
    private InternalGoogleAuthClient googleAuthClient;

    private final String route = "/user";

    @BeforeEach
    void setupMocks() {
        Mockito.doNothing().when(googleCaptchaService).verify(ArgumentMatchers.any(), ArgumentMatchers.anyString());
        Mockito.when(funixProductionsEncryptionClient.encrypt(ArgumentMatchers.anyString())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(funixProductionsEncryptionClient.decrypt(ArgumentMatchers.anyString())).thenReturn(UUID.randomUUID().toString());
        Mockito.doNothing().when(googleAuthClient).deleteAllByUserUuidIn(anyList());
    }

    @Test
    void testAccessUser() throws Exception {
        final User user = userTestComponent.createBasicUser();
        final UserTokenDTO token = userTestComponent.loginUser(user);

        mockMvc.perform(MockMvcRequestBuilders.get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testAccessModo() throws Exception {
        final User user = userTestComponent.createModoAccount();
        final UserTokenDTO token = userTestComponent.loginUser(user);

        mockMvc.perform(MockMvcRequestBuilders.get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testGetAll() throws Exception {
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createAdminAccount());

        mockMvc.perform(MockMvcRequestBuilders.get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testCreate() throws Exception {
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createAdminAccount());
        final UserSecretsDTO userDTO = createUser();

        mockMvc.perform(MockMvcRequestBuilders.post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testPatch() throws Exception {
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createAdminAccount());
        final UserSecretsDTO userDTO = createUser();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        final UserDTO user = getResponse(result);

        user.setUsername("sdkjfhsdkjh");
        mockMvc.perform(MockMvcRequestBuilders.patch(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        final UserSecretsDTO requestChangePassword = new UserSecretsDTO();
        requestChangePassword.setPassword("newPassword66GGS");
        requestChangePassword.setId(user.getId());

        mockMvc.perform(MockMvcRequestBuilders.patch(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(requestChangePassword)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        final Optional<User> search = userRepository.findByUuid(user.getId().toString());
        Assertions.assertTrue(search.isPresent());
        assertEquals(requestChangePassword.getPassword(), search.get().getPassword());
    }

    @Test
    void testFindById() throws Exception {
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createAdminAccount());
        final UserSecretsDTO userDTO = createUser();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        UserDTO user = getResponse(result);

        mockMvc.perform(MockMvcRequestBuilders.get(route + "/" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testRemoveId() throws Exception {
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createAdminAccount());
        final UserSecretsDTO userDTO = createUser();
        final String jwtToken = "jshdlqkfjhslkh";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        UserDTO user = getResponse(result);

        Mockito.when(funixProductionsEncryptionClient.encrypt(ArgumentMatchers.anyString())).thenReturn(jwtToken + UUID.randomUUID());

        UserToken userToken = new UserToken();
        userToken.setUser(userRepository.findByUuid(user.getId().toString()).get());
        userToken.setToken(jwtToken + UUID.randomUUID());
        userToken.setExpirationDate(Date.from(Instant.now().plusSeconds(10000)));
        userToken = this.userTokenRepository.save(userToken);

        mockMvc.perform(MockMvcRequestBuilders.delete(route + "?id=" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertFalse(this.userTokenRepository.findByUuid(userToken.getUuid().toString()).isPresent());
    }

    private UserDTO getResponse(final MvcResult result) throws Exception {
        return jsonHelper.fromJson(result.getResponse().getContentAsString(), UserDTO.class);
    }

    private UserSecretsDTO createUser() {
        final UserSecretsDTO userDTO = new UserSecretsDTO();

        userDTO.setUsername(UUID.randomUUID().toString());
        userDTO.setRole(UserRole.USER);
        userDTO.setEmail("oui@ggmail.com");
        userDTO.setPassword("passwsdfdsfdford11ZZ");
        return userDTO;
    }
}
