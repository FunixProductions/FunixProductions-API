package com.funixproductions.api.user.service.resources;

import com.funixproductions.api.client.user.clients.UserCrudClient;
import com.funixproductions.api.client.user.dtos.UserDTO;
import com.funixproductions.api.client.user.dtos.requests.UserSecretsDTO;
import com.funixproductions.api.service.user.services.UserCrudService;
import com.funixproductions.core.crud.dtos.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserCrudResource implements UserCrudClient {

    private final UserCrudService userCrudService;

    @Override
    public PageDTO<UserDTO> getAll(String page, String elemsPerPage, String search, String sort) {
        return userCrudService.getAll(page, elemsPerPage, search, sort);
    }

    @Override
    public UserDTO findById(String id) {
        return userCrudService.findById(id);
    }

    @Override
    public UserDTO create(UserSecretsDTO request) {
        return userCrudService.create(request);
    }

    @Override
    public UserDTO update(UserSecretsDTO request) {
        return userCrudService.update(request);
    }

    @Override
    public void delete(String id) {
        userCrudService.delete(id);
    }
}
