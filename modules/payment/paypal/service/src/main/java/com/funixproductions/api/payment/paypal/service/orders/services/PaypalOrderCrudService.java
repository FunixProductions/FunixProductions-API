package com.funixproductions.api.payment.paypal.service.orders.services;

import com.funixproductions.api.payment.paypal.service.orders.entities.OrderDTO;
import com.funixproductions.api.payment.paypal.service.orders.entities.PaypalOrder;
import com.funixproductions.api.payment.paypal.service.orders.mappers.PaypalOrderMapper;
import com.funixproductions.api.payment.paypal.service.orders.repositories.PaypalOrderRepository;
import com.funixproductions.core.crud.services.ApiService;
import org.springframework.stereotype.Service;

@Service
public class PaypalOrderCrudService extends ApiService<OrderDTO, PaypalOrder, PaypalOrderMapper, PaypalOrderRepository> {
    public PaypalOrderCrudService(PaypalOrderRepository repository,
                                  PaypalOrderMapper mapper) {
        super(repository, mapper);
    }
}
