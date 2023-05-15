package com.funixproductions.api.service.paypal.orders.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseUnitDTO {

    /**
     * Product price
     */
    private Amount amount;

    /**
     * The API caller-provided external ID. Used to reconcile client transactions with PayPal transactions. Appears in transaction and settlement reports but is not visible to the payer.
     */
    @JsonProperty(value = "custom_id")
    private String customId;

    /**
     * The purchase description. The maximum length of the character is dependent on the type of characters used. The character length is specified assuming a US ASCII character. Depending on type of character; (e.g. accented character, Japanese characters) the number of characters that that can be specified as input might not equal the permissible max length.
     */
    private String description;

    /**
     * An array of items that the customer purchases from the merchant.
     */
    private List<Item> items;

    /**
     * The merchant who receives payment for this transaction.
     */
    private Payee payee;

    /**
     * The API caller-provided external ID for the purchase unit. Required for multiple purchase units when you must update the order through PATCH. If you omit this value and the order contains only one purchase unit, PayPal sets this value to default.
     */
    @JsonProperty(value = "reference_id")
    private String referenceId;

    /**
     * The soft descriptor is the dynamic text used to construct the statement descriptor that appears on a payer's card statement.<br>
     * If an Order is paid using the "PayPal Wallet", the statement descriptor will appear in following format on the payer's card statement: PAYPAL_prefix+(space)+merchant_descriptor+(space)+ soft_descriptor
     */
    @JsonProperty(value = "soft_descriptor")
    private String softDescriptor;

    /**
     * Product price
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Amount {
        /**
         * <a href="https://developer.paypal.com/api/rest/reference/currency-codes/">Codes</a>
         */
        @JsonProperty(value = "currency_code")
        private String currencyCode;

        /**
         * 1.13 for 1€ and 13 centims
         */
        private String value;

        private Breakdown breakdown;

        /**
         * The breakdown of the amount. Breakdown provides details such as total item amount, total tax amount, shipping, handling, insurance, and discounts, if any.
         */
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Breakdown {
            @JsonProperty(value = "item_total")
            private Money itemTotal;
        }
    }

    /**
     * Product infos
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String name;

        private String quantity;

        /**
         * The item price or rate per unit. If you specify unit_amount, purchase_units[].amount.breakdown.item_total is required. Must equal unit_amount * quantity for all items. unit_amount.value can not be a negative number.
         */
        @JsonProperty(value = "unit_amount")
        private Money unitAmount;

        private Category category;

        private String description;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Money {
        @JsonProperty(value = "currency_code")
        private String currencyCode;

        private String value;
    }

    public enum Category {
        /**
         * Goods that are stored, delivered, and used in their electronic format. This value is not currently supported for API callers that leverage the PayPal for Commerce Platform product.
         */
        DIGITAL_GOODS,

        /**
         * A tangible item that can be shipped with proof of delivery.
         */
        PHYSICAL_GOODS,

        /**
         * A contribution or gift for which no good or service is exchanged, usually to a not for profit organization.
         */
        DONATION
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payee {
        /**
         * The email address of merchant.
         */
        @JsonProperty(value = "email_address")
        private String emailAddress;

        /**
         * The encrypted PayPal account ID of the merchant.
         */
        @JsonProperty(value = "merchant_id")
        private String merchantId;
    }

}