package com.funixproductions.api.user.client.clients;

import com.funixproductions.api.user.client.configs.FeignConfig;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.requests.UserSecretsDTO;
import com.funixproductions.core.crud.dtos.PageDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "UserCrud",
        url = "${funixproductions.api.user.app-domain-url}",
        path = "/user/",
        configuration = FeignConfig.class
)
public interface UserCrudClient {

    @GetMapping
    PageDTO<UserDTO> getAll(@RequestParam(value = "page", defaultValue = "0") String page,
                            @RequestParam(value = "elemsPerPage", defaultValue = "300") String elemsPerPage,
                            @RequestParam(value = "search", defaultValue = "") String search,
                            @RequestParam(value = "sort", defaultValue = "") String sort);

    @GetMapping("{id}")
    UserDTO findById(@PathVariable("id") String id);

    @PostMapping
    UserDTO create(@RequestBody @Valid UserSecretsDTO request);

    @PatchMapping
    UserDTO update(@RequestBody UserSecretsDTO request);

    @DeleteMapping
    void delete(@RequestParam String id);

}
