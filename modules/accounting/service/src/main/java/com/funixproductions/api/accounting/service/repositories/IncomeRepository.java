package com.funixproductions.api.accounting.service.repositories;

import com.funixproductions.api.accounting.service.entities.Income;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IncomeRepository extends ApiRepository<Income> {
    List<Income> findAllByCreatedAtBetween(Date start, Date end);
}
