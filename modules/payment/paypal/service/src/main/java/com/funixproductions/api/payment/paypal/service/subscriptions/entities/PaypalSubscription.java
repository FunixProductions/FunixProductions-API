package com.funixproductions.api.payment.paypal.service.subscriptions.entities;

import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "paypal_subscriptions")
public class PaypalSubscription extends ApiEntity {

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false, updatable = false)
    private PaypalPlan plan;

    @Column(name = "funix_prod_user_id", nullable = false, updatable = false)
    private String funixProdUserId;

    @Column(name = "subscription_id")
    private String subscriptionId;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "cycles_completed", nullable = false)
    private Integer cyclesCompleted;

    @Column(name = "last_payment_date")
    private Date lastPaymentDate;

    @Column(name = "next_payment_date")
    private Date nextPaymentDate;

    @Nullable
    public UUID getFunixProdUserId() {
        if (funixProdUserId == null) {
            return null;
        } else {
            return UUID.fromString(funixProdUserId);
        }
    }

    public void setFunixProdUserId(@Nullable UUID funixProdUserId) {
        if (funixProdUserId == null) {
            this.funixProdUserId = null;
        } else {
            this.funixProdUserId = funixProdUserId.toString();
        }
    }
}
