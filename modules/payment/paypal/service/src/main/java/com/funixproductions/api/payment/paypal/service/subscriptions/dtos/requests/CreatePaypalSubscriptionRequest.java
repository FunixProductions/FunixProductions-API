package com.funixproductions.api.payment.paypal.service.subscriptions.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import lombok.Getter;
import lombok.NonNull;

/**
 * <a href="https://developer.paypal.com/docs/api/subscriptions/v1/#subscriptions_create">Create sub</a>
 */
@Getter
public class CreatePaypalSubscriptionRequest {

    @JsonProperty(value = "plan_id")
    private final String planId;

    private final String quantity;

    private final Subscriber subscriber;

    @JsonProperty(value = "application_context")
    private final ApplicationContext applicationContext;

    private final Plan plan;

    public CreatePaypalSubscriptionRequest(
            @NonNull String planId,
            @NonNull UserDTO user,
            @NonNull String brand,
            @NonNull String returnUrl,
            @NonNull String cancelUrl
    ) {
        this.planId = planId;
        this.quantity = "1";
        this.subscriber = new Subscriber(user.getEmail(), user.getUsername());
        this.applicationContext = new ApplicationContext(brand, returnUrl, cancelUrl);

        final VATInformation vatInformation = VATInformation.getVATInformation(user.getCountry().getCountryCode2Chars());
        if (vatInformation != null) {
            this.plan = new Plan(vatInformation);
        } else {
            this.plan = null;
        }
    }

    @Getter
    private static class Subscriber {
        @JsonProperty(value = "email_address")
        private final String email;

        private final Name name;

        public Subscriber(String email, String username) {
            this.email = email;
            this.name = new Name(username);
        }

        @Getter
        private static class Name {
            @JsonProperty(value = "given_name")
            private final String name;

            @JsonProperty(value = "surname")
            private final String familyName;

            public Name(String username) {
                this.name = username;
                this.familyName = ".";
            }
        }

    }

    @Getter
    private static class ApplicationContext {
        @JsonProperty(value = "brand_name")
        private final String brandName;

        private final String locale;

        @JsonProperty(value = "shipping_preference")
        private final String shippingPreference;

        @JsonProperty(value = "user_action")
        private final String userAction;

        @JsonProperty(value = "return_url")
        private final String returnUrl;

        @JsonProperty(value = "cancel_url")
        private final String cancelUrl;

        @JsonProperty(value = "payment_method")
        private final PaymentMethod paymentMethod;

        public ApplicationContext(String brand, String returnUrl, String cancelUrl) {
            this.brandName = brand;
            this.locale = "fr-FR";
            this.shippingPreference = "NO_SHIPPING";
            this.userAction = "SUBSCRIBE_NOW";
            this.returnUrl = returnUrl;
            this.cancelUrl = cancelUrl;
            this.paymentMethod = new PaymentMethod();
        }

        @Getter
        private static class PaymentMethod {
            @JsonProperty(value = "payer_selected")
            private final String payerSelected;

            @JsonProperty(value = "payee_preferred")
            private final String payeePreferred;

            public PaymentMethod() {
                this.payerSelected = "PAYPAL";
                this.payeePreferred = "IMMEDIATE_PAYMENT_REQUIRED";
            }
        }
    }

    @Getter
    private static class Plan {

        private final Taxes taxes;

        public Plan(@NonNull VATInformation vatInformation) {
            this.taxes = new Taxes(vatInformation);
        }

        @Getter
        private static class Taxes {
            private final Boolean inclusive;
            private final String percentage;

            public Taxes(@NonNull VATInformation vatInformation) {
                this.inclusive = false;
                this.percentage = String.format("%.2f", vatInformation.getVatRate());
            }
        }

    }

}
