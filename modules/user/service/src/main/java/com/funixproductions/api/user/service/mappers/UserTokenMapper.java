package com.funixproductions.api.user.service.mappers;


import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.service.entities.UserToken;
import com.funixproductions.core.crud.mappers.ApiMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserTokenMapper extends ApiMapper<UserToken, UserTokenDTO> {
}
