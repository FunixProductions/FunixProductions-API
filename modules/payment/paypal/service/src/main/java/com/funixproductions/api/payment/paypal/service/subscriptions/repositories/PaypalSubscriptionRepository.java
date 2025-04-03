package com.funixproductions.api.payment.paypal.service.subscriptions.repositories;

import com.funixproductions.api.payment.paypal.service.subscriptions.entities.PaypalSubscription;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaypalSubscriptionRepository extends ApiRepository<PaypalSubscription> {
}
