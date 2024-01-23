package com.funixproductions.api.payment.billing.service.repositories;

import com.funixproductions.api.payment.billing.service.entities.Billing;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends ApiRepository<Billing> {
}
