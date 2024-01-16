package com.funixproductions.api.payment.paypal.service.orders.mappers;

import com.funixproductions.api.payment.paypal.service.orders.entities.OrderDTO;
import com.funixproductions.api.payment.paypal.service.orders.entities.PaypalOrder;
import com.funixproductions.core.crud.mappers.ApiMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaypalOrderMapper extends ApiMapper<PaypalOrder, OrderDTO> {
}
