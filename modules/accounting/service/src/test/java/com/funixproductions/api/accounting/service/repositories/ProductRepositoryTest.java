package com.funixproductions.api.accounting.service.repositories;

import com.funixproductions.api.accounting.service.entities.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testGetDataNotDuplicated() {
        final Date start = new Date();
        generateEntity(true);
        await().atLeast(1, TimeUnit.SECONDS);
        generateEntity(false);
        final Date end = Date.from(start.toInstant().plus(2, ChronoUnit.MILLIS));

        List<Product> products = this.productRepository.findAllByCreatedAtBetweenOrMonthlyIsTrue(start, end);
        assertEquals(1, products.size());
        products = this.productRepository.findAllByCreatedAtBetweenOrMonthlyIsTrue(start, Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));
        assertEquals(2, products.size());
    }

    private void generateEntity(boolean isMonthly) {
        final Product product = new Product();
        final Random random = new Random();

        product.setProductName(UUID.randomUUID().toString());
        product.setProductDescription(UUID.randomUUID().toString());
        product.setAmountHT(random.nextDouble(500));
        product.setAmountTax(random.nextDouble(500));
        product.setMonthly(isMonthly);
        product.setIsPhysical(random.nextBoolean());
        product.setIsEu(random.nextBoolean());
        this.productRepository.save(product);
    }

}
