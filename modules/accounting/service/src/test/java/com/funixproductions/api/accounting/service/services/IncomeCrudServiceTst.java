package com.funixproductions.api.accounting.service.services;

import com.funixproductions.api.accounting.client.dtos.IncomeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class IncomeCrudServiceTst {

    @Autowired
    private IncomeCrudService incomeCrudService;

    @Test
    void testCrudIncome() {
        final IncomeDTO incomeDTO = new IncomeDTO();

        incomeDTO.setIncomeName("Test Income");
        incomeDTO.setIncomeDescription("Un test de revenu");
        incomeDTO.setAmount(10.0);

        assertDoesNotThrow(() -> {
            final IncomeDTO res = incomeCrudService.create(incomeDTO);

            res.setIncomeName("tromped");
            incomeCrudService.update(res);
            incomeCrudService.delete(res.getId().toString());
        });
    }

}
