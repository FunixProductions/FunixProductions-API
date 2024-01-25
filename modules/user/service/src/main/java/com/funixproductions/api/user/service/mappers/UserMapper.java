package com.funixproductions.api.user.service.mappers;

import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.requests.UserCreationDTO;
import com.funixproductions.api.user.client.dtos.requests.UserSecretsDTO;
import com.funixproductions.api.user.client.dtos.requests.UserUpdateRequestDTO;
import com.funixproductions.api.user.service.entities.User;
import com.funixproductions.core.crud.mappers.ApiMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper extends ApiMapper<User, UserDTO> {
    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "banned", ignore = true)
    @Mapping(target = "passwordResets", ignore = true)
    @Mapping(target = "validTokens", ignore = true)
    @Mapping(target = "tokens", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "countryName", source = "country.name")
    @Mapping(target = "countryCode", source = "country.code")
    @Mapping(target = "countryCode2Chars", source = "country.countryCode2Chars")
    @Mapping(target = "countryCode3Chars", source = "country.countryCode3Chars")
    User toEntity(UserDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "country.name", source = "countryName")
    @Mapping(target = "country.code", source = "countryCode")
    @Mapping(target = "country.countryCode2Chars", source = "countryCode2Chars")
    @Mapping(target = "country.countryCode3Chars", source = "countryCode3Chars")
    UserDTO toDto(User entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "authorities", ignore = true)
    void patch(User request, @MappingTarget User toPatch);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "valid", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserSecretsDTO toSecretsDto(UserCreationDTO creationDTO);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "valid", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserSecretsDTO toSecretsDto(UserUpdateRequestDTO updateDTO);
}
