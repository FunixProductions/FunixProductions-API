package com.funixproductions.api.user.service.resources;


import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.service.services.UserTokenService;
import com.funixproductions.core.crud.resources.ApiResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/tokens")
public class UserTokenResource extends ApiResource<UserTokenDTO, UserTokenService> {

    public UserTokenResource(UserTokenService service) {
        super(service);
    }

}
