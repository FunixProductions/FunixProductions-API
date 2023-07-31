package com.funixproductions.api.user.service.ressources;

import com.funixproductions.api.core.enums.FrontOrigins;
import com.funixproductions.api.encryption.client.clients.FunixProductionsEncryptionClient;
import com.funixproductions.api.google.gmail.client.clients.GoogleGmailClient;
import com.funixproductions.api.google.recaptcha.client.clients.GoogleRecaptchaInternalClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.requests.UserPasswordResetDTO;
import com.funixproductions.api.user.client.dtos.requests.UserPasswordResetRequestDTO;
import com.funixproductions.api.user.client.dtos.requests.UserSecretsDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.entities.UserPasswordReset;
import com.funixproductions.api.user.service.repositories.UserPasswordResetRepository;
import com.funixproductions.api.user.service.services.UserCrudService;
import com.funixproductions.api.user.service.services.UserResetService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.test.beans.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Method;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserResetPasswordResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonHelper jsonHelper;

    @Autowired
    private UserCrudService userCrudService;

    @Autowired
    private UserPasswordResetRepository userPasswordResetRepository;

    @Autowired
    private UserResetService userResetService;

    @MockBean
    private GoogleRecaptchaInternalClient captchaService;

    @MockBean
    private GoogleGmailClient gmailClient;

    @MockBean
    private FunixProductionsEncryptionClient funixProductionsEncryptionClient;

    @BeforeEach
    void setupMocks() {
        doNothing().when(captchaService).verify(any(), any(), any());
        doNothing().when(gmailClient).sendMail(any(), any());
        when(funixProductionsEncryptionClient.encrypt(any())).thenReturn("encryptedText" + UUID.randomUUID());
        when(funixProductionsEncryptionClient.decrypt(any())).thenReturn("decryptedText" + UUID.randomUUID());
    }

    @Test
    void testUserResetPassword() throws Exception {
        final String email = UUID.randomUUID() + "test@gmail.com";
        final String username = "heyTest" + UUID.randomUUID();
        final UserDTO userDTO = generateUser(email, username);

        final UserPasswordResetRequestDTO userPasswordResetRequestDTO = new UserPasswordResetRequestDTO();
        userPasswordResetRequestDTO.setEmail(email);
        userPasswordResetRequestDTO.setOrigin(FrontOrigins.FUNIX_PRODUCTIONS_DASHBOARD);

        mockMvc.perform(post("/user/auth/resetPasswordRequest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(userPasswordResetRequestDTO))
        ).andExpect(status().isOk());

        final String token = getResetToken(userDTO);
        final UserPasswordResetDTO userPasswordResetDTO = new UserPasswordResetDTO();
        userPasswordResetDTO.setNewPassword("newPassword1234");
        userPasswordResetDTO.setNewPasswordConfirmation("newPassword1234");
        userPasswordResetDTO.setResetToken(token);

        mockMvc.perform(post("/user/auth/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(userPasswordResetDTO))
        ).andExpect(status().isOk());

        mockMvc.perform(post("/user/auth/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(userPasswordResetDTO))
        ).andExpect(status().isBadRequest());

        userPasswordResetDTO.setNewPassword("newPasssdfsqdfsqdword1234");
        mockMvc.perform(post("/user/auth/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.jsonHelper.toJson(userPasswordResetDTO))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testGenerateMailBody() throws Exception {
        final String userName = "heyTestUsername";
        final String clientIp = "127.0.0.1";
        final String token = "testToken";
        final FrontOrigins frontOrigin = FrontOrigins.FUNIX_PRODUCTIONS_DASHBOARD;

        final User user = new User();
        user.setUsername(userName);

        final Method method = UserResetService.class.getDeclaredMethod("generateResetMailBody", User.class, FrontOrigins.class, String.class, String.class);
        method.setAccessible(true);
        final String mailBody = (String) method.invoke(this.userResetService, user, frontOrigin, token, clientIp);

        assertTrue(mailBody.contains("<h1>Réinitialisation du mot de passe</h1>"));
        assertTrue(mailBody.contains(userName));
        assertTrue(mailBody.contains(clientIp));
        assertTrue(mailBody.contains(Base64.getEncoder().encodeToString(token.getBytes())));
        assertTrue(mailBody.contains(frontOrigin.getHumanReadableOrigin()));
    }

    private UserDTO generateUser(final String email, final String username) {
        final UserSecretsDTO userSecretsDTO = new UserSecretsDTO();

        userSecretsDTO.setPassword(UUID.randomUUID() + "1234");
        userSecretsDTO.setRole(UserRole.USER);
        userSecretsDTO.setEmail(email);
        userSecretsDTO.setUsername(username);
        return userCrudService.create(userSecretsDTO);
    }

    private String getResetToken(final UserDTO user) {
        for (final UserPasswordReset userPasswordReset : userPasswordResetRepository.findAll()) {
            if (userPasswordReset.getUser().getUuid().equals(user.getId())) {
                return Base64.getEncoder().encodeToString(userPasswordReset.getResetToken().getBytes());
            }
        }
        throw new ApiBadRequestException("No reset token found for user " + user.getUsername() + " (" + user.getId() + ")");
    }

}
