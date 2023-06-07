package com.funixproductions.api.service.user.mappers;

import com.funixproductions.api.client.user.dtos.UserDTO;
import com.funixproductions.api.client.user.dtos.requests.UserCreationDTO;
import com.funixproductions.api.client.user.dtos.requests.UserSecretsDTO;
import com.funixproductions.api.service.user.entities.User;
import com.funixproductions.core.crud.mappers.ApiMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper extends ApiMapper<User, UserDTO> {
    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "tokens", ignore = true)
    @Mapping(target = "banned", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toEntity(UserDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    UserDTO toDto(User entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "authorities", ignore = true)
    void patch(User request, @MappingTarget User toPatch);

    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tokens", ignore = true)
    @Mapping(target = "banned", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toEntity(UserSecretsDTO secretsDTO);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserSecretsDTO toSecretsDto(UserCreationDTO creationDTO);
}