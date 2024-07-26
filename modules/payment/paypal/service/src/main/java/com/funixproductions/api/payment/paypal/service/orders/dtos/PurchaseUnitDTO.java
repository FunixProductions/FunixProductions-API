package com.funixproductions.api.payment.paypal.service.orders.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
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

    @Override
    public String toString() {
        return "PurchaseUnitDTO{" +
                "amount=" + amount +
                ", customId='" + customId + '\'' +
                ", description='" + description + '\'' +
                ", items=" + items +
                ", payee=" + payee +
                ", referenceId='" + referenceId + '\'' +
                ", softDescriptor='" + softDescriptor + '\'' +
                '}';
    }

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

        @Override
        public String toString() {
            return "Amount{" +
                    "currencyCode='" + currencyCode + '\'' +
                    ", value='" + value + '\'' +
                    ", breakdown=" + breakdown +
                    '}';
        }

        /**
         * The breakdown of the amount. Breakdown provides details such as total item amount, total tax amount, shipping, handling, insurance, and discounts, if any.
         */
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Breakdown {

            /**
             * The subtotal for all items. Required if the request includes purchase_units[].items[].unit_amount. Must equal the sum of (items[].unit_amount * items[].quantity) for all items. item_total.value can not be a negative number.
             */
            @JsonProperty(value = "item_total")
            private Money itemTotal;

            /**
             * The total tax for all items. Required if the request includes purchase_units.items.tax. Must equal the sum of (items[].tax * items[].quantity) for all items. tax_total.value can not be a negative number.
             */
            @JsonProperty(value = "tax_total")
            private Money taxTotal;

            /**
             * The discount for all items within a given purchase_unit. discount.value can not be a negative number.
             */
            private Money discount;

            @Override
            public String toString() {
                return "Breakdown{" +
                        "itemTotal=" + itemTotal +
                        ", taxTotal=" + taxTotal +
                        ", discount=" + discount +
                        '}';
            }
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

        private String description;

        /**
         * The item price or rate per unit. If you specify unit_amount, purchase_units[].amount.breakdown.item_total is required. Must equal unit_amount * quantity for all items. unit_amount.value can not be a negative number.
         */
        @JsonProperty(value = "unit_amount")
        private Money unitAmount;

        /**
         * The item tax for each unit. If tax is specified, purchase_units[].amount.breakdown.tax_total is required. Must equal tax * quantity for all items. tax.value can not be a negative number.
         */
        private Money tax;

        private Category category;

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", quantity='" + quantity + '\'' +
                    ", description='" + description + '\'' +
                    ", unitAmount=" + unitAmount +
                    ", tax=" + tax +
                    ", category=" + category +
                    '}';
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Money {

        /**
         * <a href="https://developer.paypal.com/api/rest/reference/currency-codes/">Codes</a>
         * The three-character ISO-4217 currency code that identifies the currency.
         */
        @JsonProperty(value = "currency_code")
        private String currencyCode;

        private String value;

        @Override
        public String toString() {
            return "Money{" +
                    "currencyCode='" + currencyCode + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
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

        @Override
        public String toString() {
            return "Payee{" +
                    "emailAddress='" + emailAddress + '\'' +
                    ", merchantId='" + merchantId + '\'' +
                    '}';
        }
    }

}