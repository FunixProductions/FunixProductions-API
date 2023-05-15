package com.funixproductions.api.service.user.ressources;

import com.funixproductions.api.client.user.dtos.UserDTO;
import com.funixproductions.api.client.user.dtos.UserTokenDTO;
import com.funixproductions.api.client.user.dtos.requests.UserSecretsDTO;
import com.funixproductions.api.client.user.enums.UserRole;
import com.funixproductions.api.service.user.components.UserTestComponent;
import com.funixproductions.api.service.user.entities.User;
import com.funixproductions.api.service.user.entities.UserToken;
import com.funixproductions.api.service.user.repositories.UserRepository;
import com.funixproductions.api.service.user.repositories.UserTokenRepository;
import com.funixproductions.core.test.beans.JsonHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
@AutoConfigureMockMvc
class TestUserCrudResource {

    private final MockMvc mockMvc;
    private final JsonHelper jsonHelper;
    private final UserTestComponent userTestComponent;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;

    private final String route;
    private final String bearerToken;

    @Autowired
    TestUserCrudResource(MockMvc mockMvc,
                         JsonHelper jsonHelper,
                         UserTestComponent userTestComponent,
                         UserRepository userRepository,
                         UserTokenRepository userTokenRepository) throws Exception {
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createAdminAccount());

        this.mockMvc = mockMvc;
        this.jsonHelper = jsonHelper;
        this.route = "/user";
        this.bearerToken = tokenDTO.getToken();
        this.userTestComponent = userTestComponent;
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
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
        mockMvc.perform(MockMvcRequestBuilders.get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testCreate() throws Exception {
        final UserSecretsDTO userDTO = createUser();

        mockMvc.perform(MockMvcRequestBuilders.post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testPatch() throws Exception {
        final UserSecretsDTO userDTO = createUser();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        final UserDTO user = getResponse(result);

        user.setUsername("sdkjfhsdkjh");
        mockMvc.perform(MockMvcRequestBuilders.patch(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        final UserSecretsDTO requestChangePassword = new UserSecretsDTO();
        requestChangePassword.setPassword("newPassword66GGS");
        requestChangePassword.setId(user.getId());

        mockMvc.perform(MockMvcRequestBuilders.patch(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(requestChangePassword)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        final Optional<User> search = userRepository.findByUuid(user.getId().toString());
        Assertions.assertTrue(search.isPresent());
        assertEquals(requestChangePassword.getPassword(), search.get().getPassword());
    }

    @Test
    void testFindById() throws Exception {
        final UserSecretsDTO userDTO = createUser();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        UserDTO user = getResponse(result);

        mockMvc.perform(MockMvcRequestBuilders.get(route + "/" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testRemoveId() throws Exception {
        final UserSecretsDTO userDTO = createUser();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        UserDTO user = getResponse(result);

        UserToken userToken = new UserToken();
        userToken.setUser(userRepository.findByUuid(user.getId().toString()).get());
        userToken.setToken("jshdlqkfjhslkh");
        userToken.setExpirationDate(Date.from(Instant.now().plusSeconds(10000)));
        userToken = this.userTokenRepository.save(userToken);

        mockMvc.perform(MockMvcRequestBuilders.delete(route + "?id=" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
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
