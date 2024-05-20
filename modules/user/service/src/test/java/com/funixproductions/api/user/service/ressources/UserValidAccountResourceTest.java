package com.funixproductions.api.user.service.ressources;

import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.client.dtos.requests.UserCreationDTO;
import com.funixproductions.api.user.service.components.UserTestComponent;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.entities.UserValidAccountToken;
import com.funixproductions.api.user.service.repositories.UserRepository;
import com.funixproductions.api.user.service.repositories.UserValidAccountTokenRepository;
import com.funixproductions.core.test.beans.JsonHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserValidAccountResourceTest extends UserTestComponent {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserValidAccountTokenRepository userValidAccountTokenRepository;

    @Autowired
    private JsonHelper jsonHelper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSendRequestValidationEmail() throws Exception {
        final User user = createBasicUser();
        final UserTokenDTO token = loginUser(user);

        mockMvc.perform(post("/user/auth/valid-account")
                        .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(status().isOk());

        mockMvc.perform(post("/user/auth/valid-account")).andExpect(status().isUnauthorized());

        final UserValidAccountToken userValidAccountToken = userValidAccountTokenRepository.findByUser(user).get();

        mockMvc.perform(get("/user/auth/valid-account?token=" + userValidAccountToken.getValidationToken())).andExpect(status().isOk());

        final UserDTO userDTO = jsonHelper.fromJson(mockMvc.perform(get("/user/auth/current")
                        .header("Authorization", "Bearer " + loginUser(user).getToken()))
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
        final User user = createBasicUser();
        final UserTokenDTO token = loginUser(user);

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
        creationDTO.setCountry(new UserDTO.Country(
                "France",
                250,
                "FR",
                "FRA"
        ));


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

    @Test
    void testRegisterAndSendValidEmailAndChangeEmail() throws Exception {
        final UserCreationDTO creationDTO = new UserCreationDTO();
        creationDTO.setEmail(UUID.randomUUID() + "@gmail.com");
        creationDTO.setUsername(UUID.randomUUID().toString());
        creationDTO.setPassword("ousddffdi22AA");
        creationDTO.setPasswordConfirmation("ousddffdi22AA");
        creationDTO.setAcceptCGU(true);
        creationDTO.setAcceptCGV(true);
        creationDTO.setCountry(new UserDTO.Country(
                "France",
                250,
                "FR",
                "FRA"
        ));


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

        userDTO.setEmail(UUID.randomUUID() + "@gmail.com");
        mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.patch("/user/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + loginUser(user, creationDTO.getPassword()).getToken())
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final UserDTO userDTOAfterPatch = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        verify(this.googleGmailClient, times(1)).sendMail(any(), any());
        assertNotEquals(creationDTO.getEmail(), userDTOAfterPatch.getEmail());
        assertTrue(userDTOAfterPatch.getValid());
    }

    @Test
    void testRegisterAndSendValidEmailAndChangeOtherThanEmail() throws Exception {
        final UserCreationDTO creationDTO = new UserCreationDTO();
        creationDTO.setEmail(UUID.randomUUID() + "@gmail.com");
        creationDTO.setUsername(UUID.randomUUID().toString());
        creationDTO.setPassword("ousddffdi22AA");
        creationDTO.setPasswordConfirmation("ousddffdi22AA");
        creationDTO.setAcceptCGU(true);
        creationDTO.setAcceptCGV(true);
        creationDTO.setCountry(new UserDTO.Country(
                "France",
                250,
                "FR",
                "FRA"
        ));


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

        userDTO.setUsername(UUID.randomUUID().toString());
        mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.patch("/user/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + loginUser(user, creationDTO.getPassword()).getToken())
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final UserDTO userDTOAfterPatch = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        verify(this.googleGmailClient, times(1)).sendMail(any(), any());
        assertEquals(creationDTO.getEmail(), userDTOAfterPatch.getEmail());
        assertTrue(userDTOAfterPatch.getValid());
    }

}
