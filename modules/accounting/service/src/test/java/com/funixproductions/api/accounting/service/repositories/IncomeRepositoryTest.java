package com.funixproductions.api.accounting.service.repositories;

import com.funixproductions.api.accounting.service.entities.Income;
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
class IncomeRepositoryTest {

    @Autowired
    private IncomeRepository incomeRepository;

    @Test
    void testGetData() {
        final Date start = new Date();
        generateEntity();
        await().atLeast(1, TimeUnit.SECONDS);
        final Income income = generateEntity();
        await().atLeast(1, TimeUnit.SECONDS);
        generateEntity();
        await().atLeast(1, TimeUnit.SECONDS);
        final Date end = new Date();

        List<Income> incomes = this.incomeRepository.findAllByCreatedAtBetween(start, end);
        assertEquals(3, incomes.size());
        incomes = this.incomeRepository.findAllByCreatedAtBetween(new Date(), Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));
        assertEquals(0, incomes.size());
        incomes = this.incomeRepository.findAllByCreatedAtBetween(start, income.getCreatedAt());
        assertEquals(2, incomes.size());
        incomes = this.incomeRepository.findAllByCreatedAtBetween(start, Date.from(income.getCreatedAt().toInstant().minusMillis(1)));
        assertEquals(1, incomes.size());
    }

    private Income generateEntity() {
        final Income income = new Income();
        final Random random = new Random();

        income.setIncomeName(UUID.randomUUID().toString());
        income.setIncomeDescription(UUID.randomUUID().toString());
        income.setAmount(random.nextDouble(500));
        return this.incomeRepository.save(income);
    }

}
