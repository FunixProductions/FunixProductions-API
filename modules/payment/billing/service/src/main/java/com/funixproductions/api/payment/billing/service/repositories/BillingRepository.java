package com.funixproductions.api.payment.billing.service.repositories;

import com.funixproductions.api.payment.billing.service.entities.Billing;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BillingRepository extends ApiRepository<Billing> {
    List<Billing> findAllByCreatedAtBetween(Date start, Date end);
}
