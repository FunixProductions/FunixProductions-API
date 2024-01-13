package com.funixproductions.api.payment.paypal.service.orders.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.funixproductions.api.payment.paypal.service.orders.dtos.PurchaseUnitDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * <a href="https://developer.paypal.com/docs/multiparty/checkout/standard/customize/auth-capture/">Steps</a>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaypalOrderCreationDTO {

    /**
     * The intent to either capture payment immediately or authorize a payment for an order after order creation.
     */
    private PaypalIntent intent;

    /**
     * An array of purchase units. Each purchase unit establishes a contract between a payer and the payee. Each purchase unit represents either a full or partial order that the payer intends to purchase from the payee.
     */
    @JsonProperty(value = "purchase_units")
    private List<PurchaseUnitDTO> purchaseUnits;

    @JsonProperty(value = "payment_source")
    private PaymentSource paymentSource;

    /**
     * The intent to either capture payment immediately or authorize a payment for an order after order creation.
     */
    public enum PaypalIntent {
        /**
         * The merchant intends to capture payment immediately after the customer makes a payment.
         */
        CAPTURE,

        /**
         * The merchant intends to authorize a payment and place funds on hold after the customer makes a payment.
         * Authorized payments are best captured within three days of authorization but are available to capture for up to 29 days.
         * After the three-day honor period, the original authorized payment expires and you must re-authorize the payment.
         * You must make a separate request to capture payments on demand. This intent is not supported when you have more than one `purchase_unit`
         * within your order.
         */
        AUTHORIZE
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentSource {

        private Paypal paypal;

        private Card card;

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
             * The card number. The primary account number (PAN) for the payment card.
             */
            private String number;

            /**
             * The three- or four-digit security code of the card. Also known as the CVV, CVC, CVN, CVE, or CID. This parameter cannot be present in the request when payment_initiator=MERCHANT.
             */
            @JsonProperty(value = "security_code")
            private String securityCode;

            /**
             * The card expiration year and month, in Internet date format. <a href="https://tools.ietf.org/html/rfc3339#section-5.6">RFC 3339</a> Section 5.6 describes the date-time format used for Internet date formats. For example, 2020-08.
             */
            private String expiry;

            /**
             * The billing address for this card. Supports only the address_line_1, address_line_2, admin_area_1, admin_area_2, postal_code, and country_code properties.
             */
            @JsonProperty(value = "billing_address")
            private BillingAddress billingAddress;

            /**
             * Provides additional details to process a payment using a payment_source that has been stored or is intended to be stored (also referred to as stored_credential or card-on-file).<br>
             * Parameter compatibility:<br>
             * - payment_type=ONE_TIME is compatible only with payment_initiator=CUSTOMER.<br>
             * - usage=FIRST is compatible only with payment_initiator=CUSTOMER.<br>
             * - previous_transaction_reference or previous_network_transaction_reference is compatible only with payment_initiator=MERCHANT.<br>
             * - Only one of the parameters - previous_transaction_reference and previous_network_transaction_reference - can be present in the request.
             */
            @JsonProperty(value = "stored_credentials")
            private StoredCredentials storedCredentials;

            /**
             * A 3rd party network token refers to a network token that the merchant provisions from and vaults with an external TSP (Token Service Provider) other than PayPal.
             */
            @JsonProperty(value = "network_token")
            private NetworkToken networkToken;

            /**
             * Customizes the payer experience during the 3DS Approval for payment.
             */
            @JsonProperty(value = "experience_context")
            protected ExperienceContext experienceContext;

            /**
             * Customizes the payer experience during the 3DS Approval for payment.
             */
            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class ExperienceContext {
                /**
                 * The URL where the customer will be redirected upon successfully completing the 3DS challenge.
                 */
                @JsonProperty(value = "return_url")
                private String returnUrl;

                /**
                 * The URL where the customer will be redirected upon cancelling the 3DS challenge.
                 */
                @JsonProperty(value = "cancel_url")
                private String cancelUrl;
            }

            /**
             * The billing address for this card. Supports only the address_line_1, address_line_2, admin_area_1, admin_area_2, postal_code, and country_code properties.
             */
            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class BillingAddress {

                /**
                 * The first line of the address, such as number and street, for example, 173 Drury Lane. Needed for data entry, and Compliance and Risk checks. This field needs to pass the full address.
                 */
                @JsonProperty(value = "address_line_1")
                private String addressLine1;

                /**
                 * The second line of the address, such as suite or apartment number.
                 */
                @JsonProperty(value = "address_line_2")
                private String addressLine2;

                /**
                 * A city, town, or village. Smaller than admin_area_level_1.
                 */
                @JsonProperty(value = "admin_area_2")
                private String adminArea2;

                /**
                 * <div>
                 *     <p>
                 *         The highest-level sub-division in a country, which is usually a province, state, or ISO-3166-2 subdivision. This data is formatted for postal delivery, for example, CA and not California. Value, by country, is:
                 *     </p>
                 *
                 *     <p>
                 *         UK. A county.<br/>
                 *         US. A state.<br/>
                 *         Canada. A province.<br/>
                 *         Japan. A prefecture.<br/>
                 *         Switzerland. A kanton.<br/>
                 *     </p>
                 * </div>
                 */
                @JsonProperty(value = "admin_area_1")
                private String adminArea1;

                /**
                 * The postal code, which is the ZIP code or equivalent. Typically required for countries with a postal code or an equivalent. <a href="https://en.wikipedia.org/wiki/Postal_code">See postal code.</a>
                 */
                @JsonProperty(value = "postal_code")
                private String postalCode;

                /**
                 * The two-character <a href="https://developer.paypal.com/api/rest/reference/country-codes/">ISO 3166-1 code</a> that identifies the country or region.
                 */
                @JsonProperty(value = "country_code")
                private String countryCode;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class StoredCredentials {
                /**
                 * <p>The person or party who initiated or triggered the payment.</p>
                 * <p>CUSTOMER : Payment is initiated with the active engagement of the customer. e.g. a customer checking out on a merchant website.</p>
                 * <p>MERCHANT : Payment is initiated by merchant on behalf of the customer without the active engagement of customer. e.g. a merchant charging the monthly payment of a subscription to the customer.</p>
                 */
                @JsonProperty(value = "payment_initiator")
                private String paymentInitiator = "CUSTOMER";

                /**
                 * <p>Indicates the type of the stored payment_source payment.</p>
                 * <p>ONE_TIME : One Time payment such as online purchase or donation. (e.g. Checkout with one-click).</p>
                 * <p>RECURRING : Payment which is part of a series of payments with fixed or variable amounts, following a fixed time interval. (e.g. Subscription payments).</p>
                 * <p>UNSCHEDULED : Payment which is part of a series of payments that occur on a non-fixed schedule and/or have variable amounts. (e.g. Account Topup payments).</p>
                 */
                @JsonProperty(value = "payment_type")
                private String paymentType = "ONE_TIME";
            }

            /**
             * A 3rd party network token refers to a network token that the merchant provisions from and vaults with an external TSP (Token Service Provider) other than PayPal.
             */
            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class NetworkToken {
                /**
                 * Third party network token number. required
                 */
                private String number;

                /**
                 * An Encrypted one-time use value that's sent along with Network Token. This field is not required to be present for recurring transactions.
                 */
                private String cryptogram;

                /**
                 * A TRID, or a Token Requestor ID, is an identifier used by merchants to request network tokens from card networks. A TRID is a precursor to obtaining a network token for a credit card primary account number (PAN), and will aid in enabling secure card on file (COF) payments and reducing fraud.
                 */
                @JsonProperty(value = "token_requestor_id")
                private String tokenRequestorId;

                /**
                 * The card expiration year and month, in Internet date format.
                 */
                private String expiry;

                /**
                 * Electronic Commerce Indicator (ECI). The ECI value is part of the 2 data elements that indicate the transaction was processed electronically. This should be passed on the authorization transaction to the Gateway/Processor.
                 */
                @JsonProperty(value = "eci_flag")
                private EciFlag eciFlag;

                /**
                 * Electronic Commerce Indicator (ECI). The ECI value is part of the 2 data elements that indicate the transaction was processed electronically. This should be passed on the authorization transaction to the Gateway/Processor.
                 */
                public enum EciFlag {
                    /**
                     * Mastercard non-3-D Secure transaction.
                     */
                    MASTERCARD_NON_3D_SECURE_TRANSACTION,

                    /**
                     * Mastercard attempted authentication transaction.
                     */
                    MASTERCARD_ATTEMPTED_AUTHENTICATION_TRANSACTION,

                    /**
                     * Mastercard fully authenticated transaction.
                     */
                    MASTERCARD_FULLY_AUTHENTICATED_TRANSACTION,

                    /**
                     * VISA, AMEX, JCB, DINERS CLUB fully authenticated transaction.
                     */
                    FULLY_AUTHENTICATED_TRANSACTION,

                    /**
                     * VISA, AMEX, JCB, DINERS CLUB attempted authentication transaction.
                     */
                    ATTEMPTED_AUTHENTICATION_TRANSACTION,

                    /**
                     * VISA, AMEX, JCB, DINERS CLUB non-3-D Secure transaction.
                     */
                    NON_3D_SECURE_TRANSACTION
                }
            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Paypal {

            @JsonProperty(value = "experience_context")
            private ExperienceContext experienceContext;

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class ExperienceContext {
                /**
                 * The label that overrides the business name in the PayPal account on the PayPal site.
                 */
                @JsonProperty(value = "brand_name")
                private String brandName;

                /**
                 * The URL where the customer is redirected after the customer cancels the payment.
                 */
                @JsonProperty(value = "cancel_url")
                private String cancelUrl;

                /**
                 * The URL where the customer is redirected after the customer approves the payment.
                 */
                @JsonProperty(value = "return_url")
                private String returnUrl;

                /**
                 * The type of landing page to show on the PayPal site for customer checkout.
                 */
                @JsonProperty(value = "landing_page")
                private LandingPage landingPage;

                /**
                 * The BCP 47-formatted locale of pages that the PayPal payment experience shows.
                 */
                private String locale;

                /**
                 * The customer and merchant payment preferences.
                 */
                @JsonProperty(value = "payment_method")
                private PaymentMethod paymentMethod;

                /**
                 * The shipping preference
                 */
                @JsonProperty(value = "shipping_preference")
                private ShippingPreference shippingPreference;

                /**
                 * Provides additional details to process a payment using a payment_source that has been stored or is intended to be stored (also referred to as stored_credential or card-on-file).<br>
                 * Parameter compatibility:<br>
                 * - payment_type=ONE_TIME is compatible only with payment_initiator=CUSTOMER.<br>
                 * - usage=FIRST is compatible only with payment_initiator=CUSTOMER.<br>
                 * - previous_transaction_reference or previous_network_transaction_reference is compatible only with payment_initiator=MERCHANT.<br>
                 * - Only one of the parameters - previous_transaction_reference and previous_network_transaction_reference - can be present in the request.
                 */
                @JsonProperty(value = "stored_payment_source")
                private StoredPaymentSource storedPaymentSource;

                /**
                 * Configures a Continue or Pay Now checkout flow.
                 */
                @JsonProperty(value = "user_action")
                private UserAction userAction;

                public enum UserAction {
                    /**
                     * After you redirect the customer to the PayPal payment page, a Continue button appears. Use this option when the final amount is not known when the checkout flow is initiated and you want to redirect the customer to the merchant page without processing the payment.
                     */
                    CONTINUE,
                    /**
                     * After you redirect the customer to the PayPal payment page, a Pay Now button appears. Use this option when the final amount is known when the checkout is initiated and you want to process the payment immediately when the customer clicks Pay Now.
                     */
                    PAY_NOW
                }

                public enum ShippingPreference {
                    /**
                     * Use the customer-provided shipping address on the PayPal site.
                     */
                    GET_FROM_FILE,
                    /**
                     * Redact the shipping address from the PayPal site. Recommended for digital goods.
                     */
                    NO_SHIPPING,
                    /**
                     * Use the merchant-provided address. The customer cannot change this address on the PayPal site.
                     */
                    SET_PROVIDED_ADDRESS
                }

                @Getter
                @Setter
                @NoArgsConstructor
                @AllArgsConstructor
                public static class StoredPaymentSource {

                    /**
                     * The person or party who initiated or triggered the payment.
                     */
                    @JsonProperty(value = "payment_initiator")
                    private PaymentInitiator paymentInitiator;

                    /**
                     * Indicates the type of the stored payment_source payment.
                     */
                    @JsonProperty(value = "payment_type")
                    private PaymentType paymentType;

                    /**
                     * Indicates if this is a first or subsequent payment using a stored payment source (also referred to as stored credential or card on file).
                     */
                    private Usage usage;

                    public enum Usage {
                        /**
                         * Indicates the Initial/First payment with a payment_source that is intended to be stored upon successful processing of the payment.
                         */
                        FIRST,
                        /**
                         * Indicates a payment using a stored payment_source which has been successfully used previously for a payment.
                         */
                        SUBSEQUENT,
                        /**
                         * Indicates that PayPal will derive the value of `FIRST` or `SUBSEQUENT` based on data available to PayPal.
                         */
                        DERIVED
                    }

                    public enum PaymentType {
                        /**
                         * One Time payment such as online purchase or donation. (e.g. Checkout with one-click).
                         */
                        ONE_TIME,
                        /**
                         * Payment which is part of a series of payments with fixed or variable amounts, following a fixed time interval. (e.g. Subscription payments).
                         */
                        RECURRING,
                        /**
                         * Payment which is part of a series of payments that occur on a non-fixed schedule and/or have variable amounts. (e.g. Account Topup payments).
                         */
                        UNSCHEDULED
                    }

                    public enum PaymentInitiator {
                        /**
                         * Payment is initiated with the active engagement of the customer. e.g. a customer checking out on a merchant website.
                         */
                        CUSTOMER,
                        /**
                         * Payment is initiated by merchant on behalf of the customer without the active engagement of customer. e.g. a merchant charging the monthly payment of a subscription to the customer.
                         */
                        MERCHANT
                    }
                }

                @Getter
                @Setter
                @NoArgsConstructor
                @AllArgsConstructor
                public static class PaymentMethod {

                    @JsonProperty(value = "payee_preferred")
                    private PayeePreferred payeePreferred;

                    /**
                     * NACHA (the regulatory body governing the ACH network) requires that API callers (merchants, partners) obtain the consumer’s explicit authorization before initiating a transaction. To stay compliant, you’ll need to make sure that you retain a compliant authorization for each transaction that you originate to the ACH Network using this API. ACH transactions are categorized (using SEC codes) by how you capture authorization from the Receiver (the person whose bank account is being debited or credited). PayPal supports the following SEC codes.
                     */
                    @JsonProperty(value = "standard_entry_class_code")
                    private StandardEntryClassCode standardEntryClassCode;

                    public enum StandardEntryClassCode {
                        /**
                         * The API caller (merchant/partner) accepts authorization and payment information from a consumer over the telephone.
                         */
                        TEL,
                        /**
                         * The API caller (merchant/partner) accepts Debit transactions from a consumer on their website.
                         */
                        WEB,
                        /**
                         * Cash concentration and disbursement for corporate debit transaction. Used to disburse or consolidate funds. Entries are usually Optional high-dollar, low-volume, and time-critical. (e.g. intra-company transfers or invoice payments to suppliers).
                         */
                        CCD,
                        /**
                         * Prearranged payment and deposit entries. Used for debit payments authorized by a consumer account holder, and usually initiated by a company. These are usually recurring debits (such as insurance premiums).
                         */
                        PPD
                    }

                    public enum PayeePreferred {
                        /**
                         * Accepts any type of payment from the customer.
                         */
                        UNRESTRICTED,
                        /**
                         *  Accepts only immediate payment from the customer. For example, credit card, PayPal balance, or instant ACH. Ensures that at the time of capture, the payment does not have the `pending` status.
                         */
                        IMMEDIATE_PAYMENT_REQUIRED
                    }

                }

                public enum LandingPage {
                    /**
                     * When the customer clicks PayPal Checkout, the customer is redirected to a page to log in to PayPal and approve the payment.
                     */
                    LOGIN,
                    /**
                     * When the customer clicks PayPal Checkout, the customer is redirected to a page to enter credit or debit card and other relevant billing information required to complete the purchase.
                     */
                    BILLING,
                    /**
                     * When the customer clicks PayPal Checkout, the customer is redirected to either a page to log in to PayPal and approve the payment or to a page to enter credit or debit card and other relevant billing information required to complete the purchase, depending on their previous interaction with PayPal.
                     */
                    NO_PREFERENCE
                }
            }
        }

    }

}
