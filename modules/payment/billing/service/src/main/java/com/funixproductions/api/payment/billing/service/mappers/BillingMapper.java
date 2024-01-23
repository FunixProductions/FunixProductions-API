package com.funixproductions.api.payment.billing.service.mappers;

import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.service.entities.Billing;
import com.funixproductions.core.crud.mappers.ApiMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillingMapper extends ApiMapper<Billing, BillingDTO> {
}
