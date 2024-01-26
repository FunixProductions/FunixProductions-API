package com.funixproductions.api.accounting.service.entities;

import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "accounting_income")
public class Income extends ApiEntity {

    @Column(nullable = false, name = "income_name")
    private String incomeName;

    @Column(nullable = false, name = "income_description")
    private String incomeDescription;

    @Column(nullable = false, name = "amount")
    private Double amount;

}
