package com.funixproductions.api.payment.paypal.service.orders.entities;

import com.funixproductions.core.crud.entities.ApiEntity;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "paypal_orders")
public class PaypalOrder extends ApiEntity {

    @Column(name = "order_id")
    private String orderId;

    @Column(nullable = false, updatable = false, name = "user_id")
    private String userId;

    @Column(nullable = false, updatable = false, name = "user_email")
    private String userEmail;

    @Column(nullable = false, updatable = false, name = "username")
    private String username;

    @Column(nullable = false, updatable = false, name = "origin_request")
    private String originRequest;

    @Column(nullable = false, updatable = false, name = "card_payment")
    private Boolean cardPayment;

    @Column(nullable = false, name = "paid")
    private Boolean paid;

    @Enumerated(EnumType.STRING)
    @Column(name = "vat_information")
    private VATInformation vatInformation;

    @PreRemove
    void preRemove() throws ApiBadRequestException {
        throw new ApiBadRequestException("Impossible de supprimer une commande Paypal");
    }
}
