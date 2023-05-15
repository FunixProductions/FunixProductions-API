package com.funixproductions.api.service.paypal.orders.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.funixproductions.api.service.paypal.orders.dtos.PurchaseUnitDTO;
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
