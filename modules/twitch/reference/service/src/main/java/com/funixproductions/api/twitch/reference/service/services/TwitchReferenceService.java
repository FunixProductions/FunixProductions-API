package com.funixproductions.api.twitch.reference.service.services;

import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import feign.FeignException;

public abstract class TwitchReferenceService {

    protected String addBearerPrefix(final String accessToken) {
        if (accessToken.startsWith("Bearer ")) {
            return accessToken;
        } else {
            return "Bearer " + accessToken;
        }
    }

    protected ApiException handleFeignException(final FeignException e) throws ApiBadRequestException {
        final int code = e.status();
        final String errorMessage = e.getMessage();

        switch (code) {
            case 400 -> throw new ApiBadRequestException(String.format("Erreur lors de la requête Twitch: %s.", errorMessage), e);
            case 401 -> throw new ApiBadRequestException(String.format("Votre token d'accès Twitch est invalide. Erreur: %s", errorMessage), e);
            case 403 -> throw new ApiBadRequestException(String.format("Accès refusé à la ressource Twitch. Erreur: %s", errorMessage), e);
            case 404 -> throw new ApiBadRequestException(String.format("Ressouce Twitch introuvable. Erreur: %s", errorMessage), e);
            case 409 -> throw new ApiBadRequestException(String.format("Dupplication d'entitées chez twitch. Erreur: %s", errorMessage), e);
            case 429 -> throw new ApiBadRequestException("Vous faites trop de requêtes à Twitch.", e);
            default -> throw new ApiBadRequestException(String.format("Erreur fatale twitch: %s", errorMessage), e);
        }
    }
}
