package com.funixproductions.api.payment.paypal.service.subscriptions.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaypalSubscriptionResponse {

    private String id;

    @JsonProperty(value = "plan_id")
    private String planId;

    private String status;

    @JsonProperty(value = "billing_info")
    private BillingInfo billingInfo;

    private List<Link> links;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class BillingInfo {

        @JsonProperty(value = "cycle_executions")
        private List<CycleExecution> cycleExecutions;

        @JsonProperty(value = "last_payment")
        private LastPayment lastPayment;

        /**
         * The next date and time for billing this subscription, in Internet date and time format.
         */
        @JsonProperty(value = "next_billing_time")
        private String nextBillingTime;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        private static class CycleExecution {
            @JsonProperty(value = "cycles_completed")
            private Integer cyclesCompleted;
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        private static class LastPayment {
            private String status;

            /**
             * The date and time when the last payment was made, in Internet date and time format.
             */
            private String time;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Link {

        private String href;

        private String rel;

        private String method;

    }

    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public boolean isPaused() {
        return "SUSPENDED".equals(status);
    }

    public int getCyclesCompleted() {
        try {
            final BillingInfo.CycleExecution cycleExecution = billingInfo.getCycleExecutions().getFirst();

            return Objects.requireNonNullElse(cycleExecution.getCyclesCompleted(), 0);
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    @Nullable
    public Date getNextBillingDate() {
        final String time = this.billingInfo.nextBillingTime;
        if (time == null) return null;

        try {
            return Date.from(Instant.parse(time));
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public Date getLastPaymentDate() {
        if (!"COMPLETED".equals(this.billingInfo.lastPayment.status)) return null;
        final String time = this.billingInfo.lastPayment.time;
        if (time == null) return null;

        try {
            return Date.from(Instant.parse(time));
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public String getApproveLink() {
        for (final Link link : this.links) {
            if ("approve".equals(link.rel) && "GET".equalsIgnoreCase(link.method)) {
                return link.href;
            }
        }

        return null;
    }

}
