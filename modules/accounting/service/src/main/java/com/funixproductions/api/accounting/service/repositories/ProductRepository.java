package com.funixproductions.api.accounting.service.repositories;

import com.funixproductions.api.accounting.service.entities.Product;
import com.funixproductions.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends ApiRepository<Product> {
    List<Product> findAllByCreatedAtBetweenOrMonthlyIsTrue(Date start, Date end);
}
