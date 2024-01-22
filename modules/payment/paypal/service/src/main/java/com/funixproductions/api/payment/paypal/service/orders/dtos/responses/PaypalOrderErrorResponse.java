package com.funixproductions.api.payment.paypal.service.orders.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaypalOrderErrorResponse {

    private ErrorCode name;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Details {

        private String field;

        private Issue issue;

        private String description;

        @Getter
        @AllArgsConstructor
        public enum Issue {
            INVALID_ACCOUNT_STATUS("Les validations de compte ont échoué pour l'utilisateur.");

            private final String frMessage;
        }
    }

    public enum ErrorCode {
        /**
         * An internal server error occurred. A system or application error occurred.
         */
        INTERNAL_SERVER_ERROR,
        /**
         * Authentication failed due to missing authorization header, or invalid authentication credentials.
         */
        AUTHENTICATION_FAILURE,
        /**
         * The request is not well-formed, is syntactically incorrect, or violates schema. For more information, see Validation errors.
         */
        INVALID_REQUEST,
        /**
         * Authorization failed due to insufficient permissions.
         */
        NOT_AUTHORIZED,
        /**
         * The requested action could not be completed, was semantically incorrect, or failed business validation.
         */
        UNPROCESSABLE_ENTITY
    }

}
