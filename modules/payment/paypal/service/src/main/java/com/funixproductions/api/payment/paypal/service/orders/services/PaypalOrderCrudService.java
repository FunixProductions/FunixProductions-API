package com.funixproductions.api.payment.paypal.service.orders.services;

import com.funixproductions.api.payment.paypal.service.orders.entities.OrderDTO;
import com.funixproductions.api.payment.paypal.service.orders.entities.PaypalOrder;
import com.funixproductions.api.payment.paypal.service.orders.mappers.PaypalOrderMapper;
import com.funixproductions.api.payment.paypal.service.orders.repositories.PaypalOrderRepository;
import com.funixproductions.core.crud.dtos.PageDTO;
import com.funixproductions.core.crud.enums.SearchOperation;
import com.funixproductions.core.crud.services.ApiService;
import com.funixproductions.core.exceptions.ApiNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PaypalOrderCrudService extends ApiService<OrderDTO, PaypalOrder, PaypalOrderMapper, PaypalOrderRepository> {
    public PaypalOrderCrudService(PaypalOrderRepository repository,
                                  PaypalOrderMapper mapper) {
        super(repository, mapper);
    }

    public OrderDTO findByOrderId(String orderId) {
        final PageDTO<OrderDTO> pageDTO = super.getAll("0", "1", String.format("orderId:%s:%s", SearchOperation.EQUALS.getOperation(), orderId), null);

        if (pageDTO.getContent().isEmpty()) {
            throw new ApiNotFoundException("Il n'existe pas d'ordre d'achat avec cet id.");
        } else {
            return pageDTO.getContent().get(0);
        }
    }
}
