package com.funixproductions.api.payment.paypal.service.subscriptions.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses.PaypalProductResponse;
import lombok.Getter;

import java.util.List;

/**
 * <a href="https://developer.paypal.com/docs/api/subscriptions/v1/#plans_create">Create plan</a>
 */
@Getter
public class CreatePaypalPlanRequest {

    /**
     * The ID of the product created through Catalog Products API.
     */
    @JsonProperty(value = "product_id")
    private final String productId;

    /**
     * The plan name.
     */
    private final String name;

    /**
     * The initial state of the plan. Allowed input values are CREATED and ACTIVE.
     */
    private final String status;

    /**
     * The detailed description of the plan.
     */
    private final String description;

    /**
     * An array of billing cycles for trial billing and regular billing. A plan can have at most two trial cycles and only one regular cycle.
     */
    @JsonProperty(value = "billing_cycles")
    private final List<BillingCycle> billingCycles;

    @JsonProperty(value = "payment_preferences")
    private final PaymentPreference paymentPreferences;

    public CreatePaypalPlanRequest(
            PaypalProductResponse product,
            String name,
            String description,
            String price
    ) {
        this.productId = product.getId();
        this.name = name;
        this.status = "ACTIVE";
        this.description = description;
        this.billingCycles = List.of(new BillingCycle(price));
        this.paymentPreferences = new PaymentPreference();
    }

    @Getter
    private static class BillingCycle {

        /**
         * The tenure type of the billing cycle. In case of a plan having trial cycle, only 2 trial cycles are allowed per plan.
         */
        @JsonProperty(value = "tenure_type")
        private final String tenureType;

        /**
         * The order in which this cycle is to run among other billing cycles. For example, a trial billing cycle has a sequence of 1 while a regular billing cycle has a sequence of 2, so that trial cycle runs before the regular cycle.
         */
        private final Integer sequence;

        @JsonProperty(value = "total_cycles")
        private final Integer totalCycles;

        @JsonProperty(value = "pricing_scheme")
        private final PricingScheme pricingScheme;

        private final Frequency frequency;

        public BillingCycle(final String price) {
            this.tenureType = "REGULAR";
            this.sequence = 1;
            this.totalCycles = 0;
            this.pricingScheme = new PricingScheme(price);
            this.frequency = new Frequency();
        }

        @Getter
        private static class PricingScheme {
            @JsonProperty(value = "fixed_price")
            private final FixedPrice fixedPrice;

            public PricingScheme(final String value) {
                this.fixedPrice = new FixedPrice(value);
            }
        }

        @Getter
        private static class FixedPrice {
            private final String value;

            @JsonProperty(value = "currency_code")
            private final String currencyCode;

            public FixedPrice(final String value) {
                this.value = value;
                this.currencyCode = "EUR";
            }
        }

        @Getter
        private static class Frequency {
            @JsonProperty(value = "interval_unit")
            private final String intervalUnit;

            @JsonProperty(value = "interval_count")
            private final Integer interval;

            public Frequency() {
                this.intervalUnit = "MONTH";
                this.interval = 1;
            }
        }
    }

    @Getter
    private static class PaymentPreference {
        @JsonProperty(value = "auto_bill_outstanding")
        private final Boolean autoBillOutstanding;

        @JsonProperty(value = "setup_fee_failure_action")
        private final String setupFeeFailureAction;

        @JsonProperty(value = "payment_failure_threshold")
        private final Integer paymentFailureThreshold;

        public PaymentPreference() {
            this.autoBillOutstanding = true;
            this.setupFeeFailureAction = "CANCEL";
            this.paymentFailureThreshold = 3;
        }
    }

}
