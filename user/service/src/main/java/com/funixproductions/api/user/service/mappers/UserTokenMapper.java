package com.funixproductions.api.user.service.mappers;


import com.funixproductions.api.client.user.dtos.UserTokenDTO;
import com.funixproductions.api.service.user.entities.UserToken;
import com.funixproductions.core.crud.mappers.ApiMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserTokenMapper extends ApiMapper<UserToken, UserTokenDTO> {
    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    UserToken toEntity(UserTokenDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    UserTokenDTO toDto(UserToken entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(UserToken request, @MappingTarget UserToken toPatch);
}
