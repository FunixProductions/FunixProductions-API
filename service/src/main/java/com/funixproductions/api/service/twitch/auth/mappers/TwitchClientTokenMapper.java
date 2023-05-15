package com.funixproductions.api.service.twitch.auth.mappers;

import com.funixproductions.api.client.twitch.auth.dtos.TwitchClientTokenDTO;
import com.funixproductions.api.service.twitch.auth.entities.TwitchClientToken;
import com.funixproductions.api.service.user.mappers.UserMapper;
import com.funixproductions.core.crud.mappers.ApiMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TwitchClientTokenMapper extends ApiMapper<TwitchClientToken, TwitchClientTokenDTO> {

    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "OAuthCode", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    TwitchClientToken toEntity(TwitchClientTokenDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    TwitchClientTokenDTO toDto(TwitchClientToken entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(TwitchClientToken request, @MappingTarget TwitchClientToken toPatch);
}
