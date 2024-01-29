package com.funixproductions.api.accounting.service.services;

import com.funixproductions.api.accounting.client.dtos.ProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ProductCrudServiceTest {

    @Autowired
    private ProductCrudService productCrudService;

    @Test
    void testCrudProduct() {
        final ProductDTO productDTO = new ProductDTO();

        productDTO.setProductName("Test Product");
        productDTO.setProductDescription("Un test de produit");
        productDTO.setMonthly(true);
        productDTO.setIsEu(true);
        productDTO.setAmountHT(10.0);
        productDTO.setAmountTax(2.0);
        productDTO.setIsPhysical(false);

        assertDoesNotThrow(() -> {
            final ProductDTO res = productCrudService.create(productDTO);

            res.setProductName("tromped");
            productCrudService.update(res);
            productCrudService.delete(res.getId().toString());
        });
    }

}
