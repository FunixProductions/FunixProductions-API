package com.funixproductions.api.accounting.service.mappers;

import com.funixproductions.api.accounting.client.dtos.ProductDTO;
import com.funixproductions.api.accounting.service.entities.Product;
import com.funixproductions.core.crud.mappers.ApiMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends ApiMapper<Product, ProductDTO> {
}
