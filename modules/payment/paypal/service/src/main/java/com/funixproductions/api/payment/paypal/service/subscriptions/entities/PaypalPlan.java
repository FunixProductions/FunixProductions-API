package com.funixproductions.api.payment.paypal.service.subscriptions.entities;

import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Entité pour gérer les plans d'abonnements Paypal.
 */
@Getter
@Setter
@Entity(name = "paypal_plans")
public class PaypalPlan extends ApiEntity {

    @Column(name = "plan_id")
    private String planId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "home_url", nullable = false)
    private String homeUrl;

    @Column(nullable = false)
    private Double price;

    @Column(name = "project_name", nullable = false)
    private String projectName;

}
