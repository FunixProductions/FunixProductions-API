package com.funixproductions.api.payment.billing.service.services;

import com.funixproductions.api.google.gmail.client.clients.GoogleGmailClient;
import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.client.dtos.BillingObjectDTO;
import com.funixproductions.api.payment.billing.client.enums.PaymentOrigin;
import com.funixproductions.api.payment.billing.client.enums.PaymentType;
import com.funixproductions.api.payment.billing.service.entities.Billing;
import com.funixproductions.api.payment.billing.service.repositories.BillingRepository;
import com.funixproductions.api.user.client.security.CurrentSession;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
class BillingCrudServiceTest {

    @Autowired
    private BillingCrudService billingCrudService;

    @Autowired
    private BillingRepository billingRepository;

    @MockBean
    private CurrentSession currentSession;

    @MockBean
    private GoogleGmailClient googleGmailClient;

    @BeforeEach
    void setupMocks() {
        doNothing().when(googleGmailClient).sendMail(any(), any());
    }

    @Test
    void testCreateBilling() {
        final BillingDTO billingDTO = new BillingDTO();

        billingDTO.setBillingDescription("Test");
        billingDTO.setPaymentType(PaymentType.CREDIT_CARD);
        billingDTO.setBilledEntity(new BillingDTO.BilledEntity(
                "TestName",
                "TestAddress",
                "TestZipCode",
                "TestCity",
                "TestPhone",
                "TestEmail",
                "TestWebsite",
                "TestSiret",
                "TestTvaCode",
                "TestFunixProdId"
        ));
        billingDTO.setPaymentOrigin(PaymentOrigin.PACIFISTA);
        billingDTO.setAmountTotal(new BillingDTO.Price(
                40.0,
                40 * 0.2,
                40 + (40 * 0.2),
                null
        ));
        billingDTO.setVatInformation(VATInformation.FRANCE);
        billingDTO.setBillingObjects(List.of(
                new BillingObjectDTO(
                        "TestObject",
                        "TestDescription",
                        2,
                        5.0
                ),
                new BillingObjectDTO(
                        "TestObject2",
                        "TestDescription2",
                        3,
                        10.0
                )
        ));

        assertDoesNotThrow(() -> {
            final BillingDTO res = billingCrudService.create(billingDTO);
            assertNotNull(res.getId());
            assertNotNull(res.getCreatedAt());

            final Billing billing = billingRepository.findByUuid(res.getId().toString()).orElseThrow();
            assertNotNull(billing.getBillingDescription());
            assertNotNull(billing.getPaymentType());
            assertNotNull(billing.getBilledEntityName());
            assertNotNull(billing.getBilledEntityAddress());
            assertNotNull(billing.getBilledEntityZipCode());
            assertNotNull(billing.getBilledEntityCity());
            assertNotNull(billing.getBilledEntityPhone());
            assertNotNull(billing.getBilledEntityEmail());
            assertNotNull(billing.getBilledEntityWebsite());
            assertNotNull(billing.getBilledEntitySiret());
            assertNotNull(billing.getBilledEntityTvaNumber());
            assertNotNull(billing.getBilledEntityFunixProdId());
            assertNotNull(billing.getPaymentOrigin());
            assertNull(billing.getPercentageDiscount());
            assertNotNull(billing.getVatInformation());
            assertNotNull(billing.getPriceHT());
            assertNotNull(billing.getPriceTax());
            assertNotNull(billing.getPriceTTC());
            assertNotNull(billing.getInvoiceFilePath());

            final File invoiceFile = new File(billing.getInvoiceFilePath());
            assertTrue(invoiceFile.exists());
        });
    }

    @Test
    void testBlockedMethods() {
        assertThrowsExactly(ApiBadRequestException.class, () -> billingCrudService.update(new BillingDTO()));
        assertThrowsExactly(ApiBadRequestException.class, () -> billingCrudService.update(List.of()));
        assertThrowsExactly(ApiBadRequestException.class, () -> billingCrudService.updatePut(new BillingDTO()));
        assertThrowsExactly(ApiBadRequestException.class, () -> billingCrudService.updatePut(List.of()));
        assertThrowsExactly(ApiBadRequestException.class, () -> billingCrudService.delete("test"));
        assertThrowsExactly(ApiBadRequestException.class, () -> billingCrudService.delete("test", "test2"));
    }

    @Test
    void testDownloadInvoiceFromAdmin() {

    }

}
