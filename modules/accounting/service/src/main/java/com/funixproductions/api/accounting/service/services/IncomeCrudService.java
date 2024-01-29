package com.funixproductions.api.accounting.service.services;

import com.funixproductions.api.accounting.client.dtos.IncomeDTO;
import com.funixproductions.api.accounting.service.entities.Income;
import com.funixproductions.api.accounting.service.mappers.IncomeMapper;
import com.funixproductions.api.accounting.service.repositories.IncomeRepository;
import com.funixproductions.core.crud.services.ApiService;
import org.springframework.stereotype.Service;

@Service
public class IncomeCrudService extends ApiService<IncomeDTO, Income, IncomeMapper, IncomeRepository> {
    public IncomeCrudService(IncomeRepository repository, IncomeMapper mapper) {
        super(repository, mapper);
    }
}
