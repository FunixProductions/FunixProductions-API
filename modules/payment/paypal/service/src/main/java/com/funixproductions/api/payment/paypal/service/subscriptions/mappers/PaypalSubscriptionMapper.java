package com.funixproductions.api.payment.paypal.service.subscriptions.mappers;

import com.funixproductions.api.payment.paypal.client.dtos.requests.paypal.PaypalCreateSubscriptionDTO;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalSubscriptionDTO;
import com.funixproductions.api.payment.paypal.service.subscriptions.entities.PaypalSubscription;
import com.funixproductions.core.crud.mappers.ApiMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PaypalPlanMapper.class)
public interface PaypalSubscriptionMapper extends ApiMapper<PaypalSubscription, PaypalSubscriptionDTO> {

    @Mapping(target = "active", constant = "true")
    @Mapping(target = "cyclesCompleted", constant = "0")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "approveLink", ignore = true)
    @Mapping(target = "lastPaymentDate", ignore = true)
    @Mapping(target = "nextPaymentDate", ignore = true)
    @Mapping(target = "subscriptionId", ignore = true)
    @Mapping(target = "id", ignore = true)
    PaypalSubscriptionDTO toDTOFromCreation(PaypalCreateSubscriptionDTO request);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", source = "id")
    PaypalSubscription toEntity(PaypalSubscriptionDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "approveLink", ignore = true)
    PaypalSubscriptionDTO toDto(PaypalSubscription entity);

}
