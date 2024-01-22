package com.funixproductions.api.payment.paypal.service.orders.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.funixproductions.api.payment.paypal.client.enums.OrderStatus;
import com.funixproductions.api.payment.paypal.service.orders.dtos.PurchaseUnitDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter
@Setter
public class PaypalOrderResponseDTO {

    /**
     * The date and time when the transaction occurred, in <a href="https://tools.ietf.org/html/rfc3339#section-5.6">Internet date and time format</a>.
     */
    @JsonProperty(value = "create_time")
    private String createTime;

    /**
     * The date and time when the transaction was last updated, in Internet date and time format.
     */
    @JsonProperty(value = "update_time")
    private String updateTime;

    /**
     * The ID of the order.
     */
    private String id;

    /**
     * use the 'approve' type link to redirect
     */
    private List<Link> links;

    /**
     * The payment source used to fund the payment.
     */
    @JsonProperty(value = "payment_source")
    private PaymentSource paymentSource;

    /**
     * An array of purchase units. Each purchase unit establishes a contract between a customer and merchant. Each purchase unit represents either a full or partial order that the customer intends to purchase from the merchant.
     */
    @JsonProperty(value = "purchase_units")
    private List<PurchaseUnitDTO> purchaseUnits;

    /**
     * The order status.
     */
    private OrderStatus status;

    @Nullable
    public String getApproveLink() {
        return links.stream()
                .filter(Link::isApproveLink)
                .findFirst()
                .map(Link::getHref)
                .orElse(null);
    }

    @Nullable
    public String getPayerActionLink() {
        return links.stream()
                .filter(Link::isPayerActionLink)
                .findFirst()
                .map(Link::getHref)
                .orElse(null);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Link {
        /**
         * The complete target URL. To make the related call, combine the method with this <a href="https://tools.ietf.org/html/rfc6570">URI Template-formatted</a> link. For pre-processing, include the $, (, and ) characters. The href is the key HATEOAS component that links a completed call with a subsequent call.
         */
        private String href;

        /**
         * The link relation type, which serves as an ID for a link that unambiguously describes the semantics of the link. See Link Relations.
         */
        private String rel;

        private String method;

        public boolean isApproveLink() {
            return "approve".equals(rel);
        }

        public boolean isPayerActionLink() {
            return "payer-action".equals(rel);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentSource {

        private Card card;

        private PayPal paypal;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Card {
            /**
             * The cardholder's name as it appears on the card.
             */
            private String name;

            /**
             * The last digits of the payment card.
             */
            @JsonProperty(value = "last_digits")
            private String lastDigits;

            /**
             * Array of brands or networks associated with the card.
             */
            @JsonProperty(value = "available_networks")
            private String availableNetworks;

            /**
             * The card type. Valid values: `visa`, `mastercard`, `discover`, `amex`, `jcb`, `maestro`.
             */
            private String type;

            /**
             * The card brand or network. Typically used in the response.
             */
            private String brand;

            @JsonProperty(value = "authentication_result")
            private AuthenticationResult authenticationResult;

            @JsonProperty(value = "bin_details")
            private BinDetails binDetails;

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class BinDetails {

                /**
                 * The Bank Identification Number (BIN) signifies the number that is being used to identify the granular level details (except the PII information) of the card.
                 */
                private String bin;

                /**
                 * The issuer of the card instrument.
                 */
                @JsonProperty(value = "issuing_bank")
                private String issuingBank;

                /**
                 * The <a href="https://developer.paypal.com/docs/integration/direct/rest/country-codes/">two-character ISO-3166-1 country code</a> of the bank.
                 */
                @JsonProperty(value = "bin_country_code")
                private String countryCode;

            }

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class AuthenticationResult {
                @JsonProperty(value = "liability_shift")
                private LiabilityShift liabilityShift;

                @JsonProperty(value = "three_d_secure")
                private ThreeDSecure threeDSecure;

                @Getter
                @Setter
                @NoArgsConstructor
                @AllArgsConstructor
                public static class ThreeDSecure {

                    @JsonProperty(value = "authentication_status")
                    private AuthenticationStatus authenticationStatus;

                    @JsonProperty(value = "enrollment_status")
                    private EnrollmentStatus enrollmentStatus;

                    @Getter
                    @AllArgsConstructor
                    public enum EnrollmentStatus {
                        /**
                         * Yes. The bank is participating in 3-D Secure protocol and will return the ACSUrl.
                         */
                        Y(true),
                        /**
                         * No. The bank is not participating in 3-D Secure protocol.
                         */
                        N(false),
                        /**
                         * Unavailable. The DS or ACS is not available for authentication at the time of the request.
                         */
                        U(false),
                        /**
                         * Bypass. The merchant authentication rule is triggered to bypass authentication.
                         */
                        B(false);

                        private final boolean is3DSecure;
                    }

                    @Getter
                    @AllArgsConstructor
                    public enum AuthenticationStatus {
                        /**
                         * Successful authentication.
                         */
                        Y(true),
                        /**
                         * Failed authentication / account not verified / transaction denied..
                         */
                        N(false),
                        /**
                         * Unable to complete authentication.
                         */
                        U(false),
                        /**
                         * Successful attempts transaction.
                         */
                        A(true),
                        /**
                         * Challenge required for authentication.
                         */
                        C(false),
                        /**
                         * Authentication rejected (merchant must not submit for authorization).
                         */
                        R(false),
                        /**
                         * Challenge required; decoupled authentication confirmed.
                         */
                        D(true),
                        /**
                         * Informational only; 3DS requestor challenge preference acknowledged.
                         */
                        I(true);

                        private final boolean success;
                    }
                }

                /**
                 * Liability shift indicator. The outcome of the issuer's authentication.
                 */
                public enum LiabilityShift {
                    /**
                     * Liability is with the merchant.
                     */
                    NO,
                    /**
                     * Liability is with the card issuer.
                     */
                    POSSIBLE,
                    /**
                     * The authentication system is not available.
                     */
                    UNKNOWN
                }
            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PayPal {

            @JsonProperty(value = "account_status")
            private AccountStatus accountStatus;

            @JsonProperty(value = "email_address")
            private String emailAddress;

            @JsonProperty(value = "account_id")
            private String accountId;

            private Name name;

            @JsonProperty(value = "birth_date")
            private String birthDate;

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Name {
                /**
                 * When the party is a person, the party's given, or first, name.
                 */
                @JsonProperty(value = "given_name")
                private String givenName;

                /**
                 * When the party is a person, the party's surname or family name. Also known as the last name. Required when the party is a person. Use also to store multiple surnames including the matronymic, or mother's, surname.
                 */
                private String surname;
            }

            public enum AccountStatus {
                /**
                 * The buyer has completed the verification of the financial details associated with this PayPal account. For example: confirming their bank account.
                 */
                VERIFIED,
                /**
                 * The buyer has not completed the verification of the financial details associated with this PayPal account. For example: confirming their bank account.
                 */
                UNVERIFIED
            }
        }
    }

}
