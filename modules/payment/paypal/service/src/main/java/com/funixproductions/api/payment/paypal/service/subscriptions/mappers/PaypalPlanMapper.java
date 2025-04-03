package com.funixproductions.api.payment.paypal.service.subscriptions.mappers;

import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalPlanDTO;
import com.funixproductions.api.payment.paypal.service.subscriptions.entities.PaypalPlan;
import com.funixproductions.core.crud.mappers.ApiMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaypalPlanMapper extends ApiMapper<PaypalPlan, PaypalPlanDTO> {
}
