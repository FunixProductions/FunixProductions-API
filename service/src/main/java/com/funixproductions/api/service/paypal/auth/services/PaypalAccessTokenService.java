package com.funixproductions.api.service.paypal.auth.services;

import com.funixproductions.api.service.paypal.auth.clients.PaypalAuthClient;
import com.funixproductions.api.service.paypal.auth.dtos.PaypalTokenAuth;
import com.funixproductions.core.exceptions.ApiException;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaypalAccessTokenService {

    private final PaypalAuthClient authClient;
    private PaypalTokenAuth tokenAuth;

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    public void refreshPaypalToken() {
        if (tokenAuth == null || !tokenAuth.isUsable()) {
            try {
                this.tokenAuth = authClient.getToken("client_credentials");
                log.info("Nouveau token paypal généré.");
            } catch (FeignException e) {
                log.error("Impossible de générer un token paypal.", e);
            }
        }
    }

    @NonNull
    public String getAccessToken() throws ApiException {
        if (tokenAuth != null && tokenAuth.isUsable()) {
            return tokenAuth.getAccessToken();
        } else {
            throw new ApiException("Le token paypal n'est plus valide.");
        }
    }

}
