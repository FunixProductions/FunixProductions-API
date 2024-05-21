package com.funixproductions.api.user.service.services;

import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.requests.UserSecretsDTO;
import com.funixproductions.api.user.client.dtos.requests.UserUpdateRequestDTO;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.google.common.base.Strings;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserUpdateAccountService {

    private final UserCrudService userCrudService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO updateUser(final UserUpdateRequestDTO request, final UserDTO currentUser) {
        final UserSecretsDTO updateDto = this.userCrudService.getMapper().toSecretsDto(request);

        checkPassword(request, updateDto, currentUser);
        updateDto.setId(currentUser.getId());
        updateDto.setValid(currentUser.getValid());
        updateDto.setRole(currentUser.getRole());
        return this.userCrudService.update(updateDto);
    }

    private void checkPassword(final UserUpdateRequestDTO request,
                               final UserSecretsDTO updateRequest,
                               final UserDTO currentUser) {
        if (!Strings.isNullOrEmpty(request.getNewPassword()) &&
                !Strings.isNullOrEmpty(request.getNewPasswordConfirmation()) &&
                !Strings.isNullOrEmpty(request.getOldPassword())) {
            if (request.getNewPassword().equals(request.getNewPasswordConfirmation())) {
                checkOldPasswordEquals(request.getOldPassword(), currentUser.getId());
                updateRequest.setPassword(request.getNewPassword());
            } else {
                throw new ApiBadRequestException("Vos mots de passe ne correspondent pas.");
            }
        }
    }

    private void checkOldPasswordEquals(@NonNull final String oldPassword, @NonNull final UUID userId) {
        final Optional<User> search = this.userCrudService.getRepository().findByUuid(userId.toString());

        if (search.isPresent()) {
            final User user = search.get();

            if (!this.passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new ApiBadRequestException("L'ancien mot de passe n'est pas valide.");
            }
        } else {
            throw new ApiBadRequestException("L'utilisateur courant n'existe plus.");
        }
    }

}
