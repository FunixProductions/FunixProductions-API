package com.funixproductions.api.user.service.services;

import com.funixproductions.api.google.auth.client.clients.InternalGoogleAuthClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.requests.UserSecretsDTO;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.mappers.UserMapper;
import com.funixproductions.api.user.service.repositories.UserRepository;
import com.funixproductions.core.crud.services.ApiService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserCrudService extends ApiService<UserDTO, User, UserMapper, UserRepository> implements UserDetailsService {

    private final UserValidationAccountService validationAccountService;
    private final InternalGoogleAuthClient googleAuthClient;
    private final PasswordEncoder passwordEncoder;

    public UserCrudService(UserMapper mapper,
                           UserRepository repository,
                           UserValidationAccountService validationAccountService,
                           InternalGoogleAuthClient googleAuthClient,
                           PasswordEncoder passwordEncoder) {
        super(repository, mapper);
        this.validationAccountService = validationAccountService;
        this.googleAuthClient = googleAuthClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void beforeMappingToEntity(@NonNull Iterable<UserDTO> requestList) {
        final List<String> ids = new ArrayList<>();

        for (final UserDTO request : requestList) {
            if (request.getId() != null) {
                ids.add(request.getId().toString());
            }
        }

        final Iterable<User> users = this.getRepository().findAllByUuidIn(ids);

        this.validAccountCheckerFilter(requestList, users);
        for (final UserDTO request : requestList) {
            this.checkUsernameFilter(request);
        }
    }

    @Override
    public void afterSavingEntity(@NonNull Iterable<User> entity) {
        for (final User user : entity) {
            if (Boolean.FALSE.equals(user.getValid())) {
                this.validationAccountService.sendMailValidationRequest(user);
            }
        }
    }

    @Override
    public void afterMapperCall(@NonNull UserDTO dto, @NonNull User entity) {
        if (dto instanceof final UserSecretsDTO secretsDTO && Strings.isNotBlank(secretsDTO.getPassword())) {
            entity.setPassword(this.passwordEncoder.encode(secretsDTO.getPassword()));
        }
    }

    @Override
    public User loadUserByUsername(String username) throws ApiNotFoundException {
        return super.getRepository()
                .findByUsername(username)
                .orElseThrow(
                        () -> new ApiNotFoundException(String.format("Utilisateur %s non trouvé", username))
                );
    }

    @Override
    public void beforeDeletingEntity(@NonNull Iterable<User> entity) {
        final ArrayList<String> uuidsToRemove = new ArrayList<>();

        for (final User user : entity) {
            uuidsToRemove.add(user.getUuid().toString());
        }
        this.googleAuthClient.deleteAllByUserUuidIn(uuidsToRemove.toArray(new String[0]));
    }

    private void validAccountCheckerFilter(final Iterable<UserDTO> requestList, final Iterable<User> users) {
        for (final UserDTO request : requestList) {
            final User userDatabase = super.getEntityFromUidInList(users, request.getId());

            if (userDatabase != null) {
                request.setValid(userDatabase.getValid());
            }
        }
    }

    private void checkUsernameFilter(@NonNull final UserDTO request) {
        if (request.getId() == null) {
            final Optional<User> search = this.getRepository().findByUsernameIgnoreCase(request.getUsername());
            if (search.isPresent()) {
                throw new ApiBadRequestException(String.format("L'utilisateur %s existe déjà.", request.getUsername()));
            }
        }
    }
}
