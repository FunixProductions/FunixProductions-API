package com.funixproductions.api.payment.billing.service.mappers;

import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.service.entities.Billing;
import com.funixproductions.core.crud.mappers.ApiMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillingMapper extends ApiMapper<Billing, BillingDTO> {

    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "billedEntityName", source = "billedEntity.name")
    @Mapping(target = "billedEntityAddress", source = "billedEntity.address")
    @Mapping(target = "billedEntityZipCode", source = "billedEntity.zipCode")
    @Mapping(target = "billedEntityCity", source = "billedEntity.city")
    @Mapping(target = "billedEntityPhone", source = "billedEntity.phone")
    @Mapping(target = "billedEntityEmail", source = "billedEntity.email")
    @Mapping(target = "billedEntityWebsite", source = "billedEntity.website")
    @Mapping(target = "billedEntitySiret", source = "billedEntity.siret")
    @Mapping(target = "billedEntityTvaNumber", source = "billedEntity.tvaCode")
    @Mapping(target = "billedEntityFunixProdId", source = "billedEntity.userFunixProdId")
    @Mapping(target = "priceHT", source = "amountTotal.ht")
    @Mapping(target = "priceTTC", source = "amountTotal.ttc")
    @Mapping(target = "priceTax", source = "amountTotal.tax")
    @Mapping(target = "percentageDiscount", source = "amountTotal.discount")
    Billing toEntity(BillingDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "amountTotal.ht", source = "priceHT")
    @Mapping(target = "amountTotal.ttc", source = "priceTTC")
    @Mapping(target = "amountTotal.tax", source = "priceTax")
    @Mapping(target = "billedEntity.name", source = "billedEntityName")
    @Mapping(target = "billedEntity.address", source = "billedEntityAddress")
    @Mapping(target = "billedEntity.zipCode", source = "billedEntityZipCode")
    @Mapping(target = "billedEntity.city", source = "billedEntityCity")
    @Mapping(target = "billedEntity.phone", source = "billedEntityPhone")
    @Mapping(target = "billedEntity.email", source = "billedEntityEmail")
    @Mapping(target = "billedEntity.website", source = "billedEntityWebsite")
    @Mapping(target = "billedEntity.siret", source = "billedEntitySiret")
    @Mapping(target = "billedEntity.tvaCode", source = "billedEntityTvaNumber")
    @Mapping(target = "billedEntity.userFunixProdId", source = "billedEntityFunixProdId")
    @Mapping(target = "billingObjects", ignore = true)
    BillingDTO toDto(Billing entity);

}
