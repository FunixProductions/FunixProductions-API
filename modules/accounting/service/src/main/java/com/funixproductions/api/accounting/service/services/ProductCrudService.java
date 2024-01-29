package com.funixproductions.api.accounting.service.services;

import com.funixproductions.api.accounting.client.dtos.ProductDTO;
import com.funixproductions.api.accounting.service.entities.Product;
import com.funixproductions.api.accounting.service.mappers.ProductMapper;
import com.funixproductions.api.accounting.service.repositories.ProductRepository;
import com.funixproductions.core.crud.services.ApiService;
import org.springframework.stereotype.Service;

@Service
public class ProductCrudService extends ApiService<ProductDTO, Product, ProductMapper, ProductRepository> {
    public ProductCrudService(ProductRepository repository,
                              ProductMapper mapper) {
        super(repository, mapper);
    }
}
