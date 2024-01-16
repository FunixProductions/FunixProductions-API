package com.funixproductions.api.payment.paypal.service.orders.repositories;

import com.funixproductions.api.payment.paypal.service.orders.entities.PaypalOrder;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaypalOrderRepository extends ApiRepository<PaypalOrder> {
}
