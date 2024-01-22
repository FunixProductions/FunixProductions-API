package com.funixproductions.api.user.service.services;

import com.funixproductions.api.google.auth.client.clients.InternalGoogleAuthClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.requests.UserSecretsDTO;
import com.funixproductions.api.user.service.components.UserPasswordUtils;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.api.user.service.mappers.UserMapper;
import com.funixproductions.api.user.service.repositories.UserRepository;
import com.funixproductions.core.crud.services.ApiService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiNotFoundException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserCrudService extends ApiService<UserDTO, User, UserMapper, UserRepository> implements UserDetailsService {

    private final UserPasswordUtils passwordUtils;
    private final UserValidationAccountService validationAccountService;
    private final InternalGoogleAuthClient googleAuthClient;

    private final Cache<Long, String> emailMapperCheck = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.MINUTES).build();

    public UserCrudService(UserMapper mapper,
                           UserRepository repository,
                           UserPasswordUtils passwordUtils,
                           UserValidationAccountService validationAccountService,
                           InternalGoogleAuthClient googleAuthClient) {
        super(repository, mapper);
        this.passwordUtils = passwordUtils;
        this.validationAccountService = validationAccountService;
        this.googleAuthClient = googleAuthClient;
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
    public void beforeSavingEntity(@NonNull Iterable<User> users) {
        for (final User user : users) {
            if (user.getId() != null && user.getEmail() != null) {
                final String email = user.getEmail();
                final Long id = user.getId();

                this.emailMapperCheck.put(id, email);
            }
        }
    }

    @Override
    public void afterSavingEntity(@NonNull Iterable<User> entity) {
        for (final User user : entity) {
            if (Boolean.FALSE.equals(user.getValid())) {
                this.emailMapperCheck.invalidate(user.getId());
                this.validationAccountService.sendMailValidationRequest(user);
                continue;
            }

            final String emailCache = this.emailMapperCheck.getIfPresent(user.getId());
            if (emailCache != null && !emailCache.equals(user.getEmail())) {
                this.emailMapperCheck.invalidate(user.getId());
                user.setValid(false);
                super.getRepository().save(user);
                this.validationAccountService.sendMailValidationRequest(user);
            }
        }
    }

    @Override
    public void afterMapperCall(@NonNull UserDTO dto, @NonNull User entity) {
        if (dto instanceof final UserSecretsDTO secretsDTO && Strings.isNotBlank(secretsDTO.getPassword())) {
            passwordUtils.checkPassword(secretsDTO.getPassword());
            entity.setPassword(secretsDTO.getPassword());
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
        final List<String> uuidsToRemove = new ArrayList<>();

        for (final User user : entity) {
            uuidsToRemove.add(user.getUuid().toString());
        }
        this.googleAuthClient.deleteAllByUserUuidIn(uuidsToRemove);
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
        if (request.getUsername() != null && !checkUsernameHasValidCharacters(request.getUsername())) {
            throw new ApiBadRequestException("Le nom d'utilisateur ne peut contenir que des lettres, des chiffres, des underscores et des tirets. Sans espaces.");
        }

        if (request.getId() == null) {
            final Optional<User> search = this.getRepository().findByUsernameIgnoreCase(request.getUsername());
            if (search.isPresent()) {
                throw new ApiBadRequestException(String.format("L'utilisateur %s existe déjà.", request.getUsername()));
            }
        }
    }

    /**
     * Valid majs, mins, - and _
     * @param username
     * @return
     */
    private boolean checkUsernameHasValidCharacters(@NonNull String username) {
        return Strings.isNotBlank(username) && username.matches("^[a-zA-Z0-9._-]{3,}$");
    }
}
