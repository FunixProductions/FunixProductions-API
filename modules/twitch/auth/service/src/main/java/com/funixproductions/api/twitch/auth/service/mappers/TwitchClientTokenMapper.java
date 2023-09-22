package com.funixproductions.api.twitch.auth.service.mappers;

import com.funixproductions.api.twitch.auth.client.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.twitch.auth.service.entities.TwitchClientToken;
import com.funixproductions.core.crud.mappers.ApiMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TwitchClientTokenMapper extends ApiMapper<TwitchClientToken, TwitchClientTokenDTO> {

    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "OAuthCode", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    TwitchClientToken toEntity(TwitchClientTokenDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "scopes", ignore = true)
    TwitchClientTokenDTO toDto(TwitchClientToken entity);

}
