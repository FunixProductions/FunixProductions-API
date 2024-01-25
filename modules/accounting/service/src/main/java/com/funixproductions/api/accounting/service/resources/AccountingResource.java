package com.funixproductions.api.accounting.service.resources;

import com.funixproductions.api.accounting.client.dtos.ProductDTO;
import com.funixproductions.api.accounting.service.services.ProductCrudService;
import com.funixproductions.core.crud.resources.ApiResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounting")
public class AccountingResource extends ApiResource<ProductDTO, ProductCrudService> {
    public AccountingResource(ProductCrudService productCrudService) {
        super(productCrudService);
    }
}
