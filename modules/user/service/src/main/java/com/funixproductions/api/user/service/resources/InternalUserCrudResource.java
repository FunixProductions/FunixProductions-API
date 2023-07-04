package com.funixproductions.api.user.service.resources;

import com.funixproductions.api.user.client.clients.InternalUserCrudClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.UserTokenDTO;
import com.funixproductions.api.user.service.services.UserCrudService;
import com.funixproductions.api.user.service.services.UserTokenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("kubeinternal/user")
public class InternalUserCrudResource extends UserCrudResource implements InternalUserCrudClient {

    private final UserTokenService userTokenService;

    public InternalUserCrudResource(UserCrudService userCrudService,
                                    UserTokenService userTokenService) {
        super(userCrudService);
        this.userTokenService = userTokenService;
    }

    @Override
    public UserTokenDTO generateAccessToken(UserDTO userDTO) {
        return this.userTokenService.generateAccessToken(userDTO);
    }
}
