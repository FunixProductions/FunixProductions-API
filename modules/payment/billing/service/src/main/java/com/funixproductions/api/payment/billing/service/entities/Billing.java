package com.funixproductions.api.payment.billing.service.entities;

import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "funixproductions_billing")
public class Billing extends ApiEntity {
}
