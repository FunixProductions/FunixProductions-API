package com.funixproductions.api.accounting.service.mappers;

import com.funixproductions.api.accounting.client.dtos.IncomeDTO;
import com.funixproductions.api.accounting.service.entities.Income;
import com.funixproductions.core.crud.mappers.ApiMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IncomeMapper extends ApiMapper<Income, IncomeDTO> {
}
