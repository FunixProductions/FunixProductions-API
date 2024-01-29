package com.funixproductions.api.accounting.service.services;

import com.funixproductions.api.accounting.service.entities.Income;
import com.funixproductions.api.accounting.service.entities.Product;
import com.funixproductions.api.accounting.service.repositories.IncomeRepository;
import com.funixproductions.api.accounting.service.repositories.ProductRepository;
import com.funixproductions.api.google.gmail.client.clients.GoogleGmailClient;
import com.funixproductions.api.payment.billing.client.clients.BillingFeignInternalClient;
import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.client.enums.PaymentOrigin;
import com.funixproductions.api.payment.billing.client.enums.PaymentType;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountingServiceTest {

    @Autowired
    private AccountingService accountingService;

    @MockBean
    private IncomeRepository incomeRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private GoogleGmailClient gmailClient;

    @MockBean
    private BillingFeignInternalClient billingClient;

    @Test
    void testGenerateInvoice() {
        final Product product = generateProductEntity();
        product.setIsEu(false);
        product.setIsPhysical(false);

        final List<Product> productList = List.of(
                generateProductEntity(),
                generateProductEntity(),
                generateProductEntity(),
                product
        );
        final List<Income> incomeList = List.of(
                generateIncomeEntity(),
                generateIncomeEntity(),
                generateIncomeEntity()
        );
        final List<BillingDTO> billingDTOS = List.of(
                generateBilling(),
                generateBilling(),
                generateBilling()
        );
        when(this.productRepository.findAllByCreatedAtBetweenOrMonthlyIsTrue(any(), any())).thenReturn(productList);
        when(this.incomeRepository.findAllByCreatedAtBetween(any(), any())).thenReturn(incomeList);
        when(this.billingClient.getMonthlyReport(any())).thenReturn(billingDTOS);
        doNothing().when(this.gmailClient).sendMail(any(), any());

        assertDoesNotThrow(() -> this.accountingService.sendLastMonthBillingReport());
    }

    private BillingDTO generateBilling() {
        final BillingDTO billingDTO = new BillingDTO();
        final Random random = new Random();

        billingDTO.setBillingDescription(UUID.randomUUID().toString());
        billingDTO.setPaymentType(PaymentType.PAYPAL);
        billingDTO.setBilledEntity(new BillingDTO.BilledEntity());
        billingDTO.setPaymentOrigin(PaymentOrigin.FUNIXGAMING);
        billingDTO.setAmountTotal(new BillingDTO.Price(
                random.nextDouble(500),
                random.nextDouble(500),
                random.nextDouble(500),
                null
        ));
        billingDTO.setVatInformation(random.nextBoolean() ? VATInformation.FRANCE : null);
        billingDTO.setBillingObjects(List.of());
        return billingDTO;
    }

    private Income generateIncomeEntity() {
        final Random random = new Random();
        final Income income = new Income();

        income.setId(random.nextLong(1000));
        income.setUuid(UUID.randomUUID());
        income.setIncomeName(UUID.randomUUID().toString());
        income.setIncomeDescription(UUID.randomUUID().toString());
        income.setAmount(random.nextDouble(500));
        return income;
    }

    private Product generateProductEntity() {
        final Product product = new Product();
        final Random random = new Random();

        product.setId(random.nextLong(1000));
        product.setUuid(UUID.randomUUID());
        product.setProductName(UUID.randomUUID().toString());
        product.setProductDescription(UUID.randomUUID().toString());
        product.setAmountHT(random.nextDouble(500));
        product.setAmountTax(random.nextDouble(500));
        product.setMonthly(random.nextBoolean());
        product.setIsPhysical(random.nextBoolean());
        product.setIsEu(random.nextBoolean());
        return product;
    }

}
