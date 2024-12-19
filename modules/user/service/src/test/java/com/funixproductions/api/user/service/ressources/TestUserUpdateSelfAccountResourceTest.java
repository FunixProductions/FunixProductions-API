package com.funixproductions.api.user.service.ressources;

import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.client.dtos.requests.UserLoginDTO;
import com.funixproductions.api.user.client.dtos.requests.UserUpdateRequestDTO;
import com.funixproductions.api.user.service.components.UserTestComponent;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.services.UserValidationAccountService;
import com.funixproductions.core.test.beans.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TestUserUpdateSelfAccountResourceTest extends UserTestComponent {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonHelper jsonHelper;

    @MockitoBean
    private UserValidationAccountService userValidationAccountService;

    @BeforeEach
    void setupMocks() {
        doNothing().when(userValidationAccountService).validateAccount(anyString());
        doNothing().when(userValidationAccountService).sendMailValidationRequest(any(User.class));
        doNothing().when(userValidationAccountService).sendMailValidationRequest(any(UUID.class));
    }

    @Test
    void testUpdateSelfAccountPasswordSuccess() throws Exception {
        final User userTest = this.createBasicUser();
        final UserTokenDTO tokenDTO = this.loginUser(userTest);

        final String newPassword = "superNewPassword22";
        final UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO();
        userUpdateRequestDTO.setNewPassword(newPassword);
        userUpdateRequestDTO.setNewPasswordConfirmation(newPassword);
        userUpdateRequestDTO.setOldPassword(UserTestComponent.PASSWORD);

        this.mockMvc.perform(patch("/user/auth")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(userUpdateRequestDTO))
        ).andExpect(status().isOk());

        userUpdateRequestDTO.setNewPassword(null);
        userUpdateRequestDTO.setNewPasswordConfirmation(null);
        userUpdateRequestDTO.setOldPassword(null);
        MvcResult mvcResult = this.mockMvc.perform(patch("/user/auth")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(userUpdateRequestDTO))
        ).andExpect(status().isOk()).andReturn();

        final UserDTO res = this.jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(userTest.getRole(), res.getRole());
    }

    @Test
    void testUpdateSelfAccountAdminPasswordSuccess() throws Exception {
        final User userTest = this.createAdminAccount();
        final UserTokenDTO tokenDTO = this.loginUser(userTest);

        final String newPassword = "superNewPassword22";
        final UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO();
        userUpdateRequestDTO.setNewPassword(newPassword);
        userUpdateRequestDTO.setNewPasswordConfirmation(newPassword);
        userUpdateRequestDTO.setOldPassword(UserTestComponent.PASSWORD);

        this.mockMvc.perform(patch("/user/auth")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(userUpdateRequestDTO))
        ).andExpect(status().isOk());

        userUpdateRequestDTO.setNewPassword(null);
        userUpdateRequestDTO.setNewPasswordConfirmation(null);
        userUpdateRequestDTO.setOldPassword(null);
        MvcResult mvcResult = this.mockMvc.perform(patch("/user/auth")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(userUpdateRequestDTO))
        ).andExpect(status().isOk()).andReturn();

        final UserDTO res = this.jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(userTest.getRole(), res.getRole());
    }

    @Test
    void testUpdateSelfAccountPasswordFailNewPasswordMissMatch() throws Exception {
        final User userTest = this.createBasicUser();
        final UserTokenDTO tokenDTO = this.loginUser(userTest);

        final UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO();
        userUpdateRequestDTO.setNewPassword("newPassword");
        userUpdateRequestDTO.setNewPasswordConfirmation("newPasswordMissMatch");
        userUpdateRequestDTO.setOldPassword(UserTestComponent.PASSWORD);

        this.mockMvc.perform(patch("/user/auth")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(userUpdateRequestDTO))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateSelfAccountPasswordFailOldPasswordNoMatch() throws Exception {
        final User userTest = this.createBasicUser();
        final UserTokenDTO tokenDTO = this.loginUser(userTest);

        final String newPassword = "superNewPassword";
        final UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO();
        userUpdateRequestDTO.setNewPassword(newPassword);
        userUpdateRequestDTO.setNewPasswordConfirmation(newPassword);
        userUpdateRequestDTO.setOldPassword(UUID.randomUUID().toString());

        this.mockMvc.perform(patch("/user/auth")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(userUpdateRequestDTO))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateSelfAccountOnlyPersonalDataSuccess() throws Exception {
        final User userTest = this.createBasicUser();
        final UserTokenDTO tokenDTO = this.loginUser(userTest);

        final UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO();
        userUpdateRequestDTO.setUsername("newUsername");
        userUpdateRequestDTO.setEmail("sdqlmfjsmlfjjqsldf1@gmail.com");
        userUpdateRequestDTO.setCountry(new UserDTO.Country(
                "test",
                16,
                "te",
                "tes"
        ));

        MvcResult mvcResult = this.mockMvc.perform(patch("/user/auth")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(userUpdateRequestDTO))
        ).andExpect(status().isOk()).andReturn();
        final UserDTO res = this.jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(userTest.getRole(), res.getRole());
        assertEquals(userUpdateRequestDTO.getUsername(), res.getUsername());
        assertEquals(userUpdateRequestDTO.getEmail(), res.getEmail());
        assertEquals(userUpdateRequestDTO.getCountry(), res.getCountry());

        res.setEmail(null);
        mvcResult = this.mockMvc.perform(patch("/user/auth")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(res))
        ).andExpect(status().isOk()).andReturn();
        final UserDTO res2 = this.jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(userUpdateRequestDTO.getEmail(), res2.getEmail());
        assertEquals(userUpdateRequestDTO.getUsername(), res2.getUsername());

        final UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(userUpdateRequestDTO.getUsername());
        userLoginDTO.setPassword(UserTestComponent.PASSWORD);

        this.mockMvc.perform(post("/user/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(userLoginDTO))
        ).andExpect(status().isOk());
    }

    @Test
    void testUpdateSelfAccountOnlyPasswordDataSuccess() throws Exception {
        final User userTest = this.createBasicUser();
        final UserTokenDTO tokenDTO = this.loginUser(userTest);

        final UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO();
        final String newPassword = "superNewPassword22Hourra";
        userUpdateRequestDTO.setNewPassword(newPassword);
        userUpdateRequestDTO.setNewPasswordConfirmation(newPassword);
        userUpdateRequestDTO.setOldPassword(UserTestComponent.PASSWORD);

        this.mockMvc.perform(patch("/user/auth")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(userUpdateRequestDTO))
        ).andExpect(status().isOk());

        final UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(userTest.getUsername());
        userLoginDTO.setPassword(newPassword);

        this.mockMvc.perform(post("/user/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(userLoginDTO))
        ).andExpect(status().isOk());
    }
}
