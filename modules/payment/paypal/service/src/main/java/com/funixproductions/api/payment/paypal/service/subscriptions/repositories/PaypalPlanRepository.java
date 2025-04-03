package com.funixproductions.api.payment.paypal.service.subscriptions.repositories;

import com.funixproductions.api.payment.paypal.service.subscriptions.entities.PaypalPlan;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaypalPlanRepository extends ApiRepository<PaypalPlan> {
    boolean existsByNameAndProjectName(String name, String projectName);
}
