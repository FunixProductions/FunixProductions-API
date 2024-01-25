package com.funixproductions.api.accounting.service.entities;

import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "accounting_product")
public class Product extends ApiEntity {

    @Column(nullable = false, name = "product_name")
    private String productName;

    @Column(nullable = false, name = "product_description")
    private String productDescription;

    @Column(nullable = false, name = "monthly")
    private Boolean monthly;

    @Column(nullable = false, name = "amount_ht")
    private Double amountHT;

    @Column(nullable = false, name = "amount_tax")
    private Double amountTax;
}
