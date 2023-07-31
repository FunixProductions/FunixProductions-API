package com.funixproductions.api.user.service.components;

import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordUtils {

    private static final int MINIMUM_LENGTH = 8;
    private static final int MINIMUM_NUMBERS = 2;

    /**
     * Check if the password is strong enough for app usage
     * @param password password string
     * @throws ApiBadRequestException if the password is not valid
     */
    public void checkPassword(final String password) throws ApiBadRequestException {
        if (Strings.isNullOrEmpty(password)) {
            throw new ApiBadRequestException("Votre mot de passe est vide.");
        }

        if (password.length() < MINIMUM_LENGTH) {
            throw new ApiBadRequestException("Votre mot de passe doit au minimum avoir au moins " + MINIMUM_LENGTH + " caractères.");
        } else if (passwordWeakNumbers(password)) {
            throw new ApiBadRequestException("Votre mot de passe doit contenir au moins " + MINIMUM_NUMBERS + " chiffres.");
        }
    }

    private boolean passwordWeakNumbers(final String password) {
        int numbersCount = 0;

        for (char c : password.toCharArray()) {
            if (c >= '0' && c <= '9') {
                ++numbersCount;
            }
        }
        return numbersCount < MINIMUM_NUMBERS;
    }
}
