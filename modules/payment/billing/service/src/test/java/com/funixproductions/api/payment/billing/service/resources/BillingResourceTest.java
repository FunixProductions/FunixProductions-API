package com.funixproductions.api.payment.billing.service.resources;

import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.client.dtos.BillingObjectDTO;
import com.funixproductions.api.payment.billing.client.enums.PaymentOrigin;
import com.funixproductions.api.payment.billing.client.enums.PaymentType;
import com.funixproductions.api.payment.billing.service.services.BillingCrudService;
import com.funixproductions.api.user.client.clients.UserAuthClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.core.crud.dtos.PageDTO;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public abstract class BillingResourceTest {

    @MockBean
    protected UserAuthClient userAuthClient;

    @MockBean
    protected BillingCrudService billingCrudService;

    protected final BillingDTO billingDTO = new BillingDTO();

    @BeforeEach
    void setUp() {
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
                UUID.randomUUID().toString()
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

        when(billingCrudService.getAll(any(), any(), any(), any())).thenReturn(new PageDTO<>(List.of(billingDTO), 1, 0, 1L, 1));
        when(billingCrudService.findById(any())).thenReturn(billingDTO);
        when(billingCrudService.getInvoiceFile(any())).thenReturn(new ByteArrayResource(new byte[0]));
    }

    protected void setupAuth(final UserRole role) {
        reset(userAuthClient);

        final UserDTO userDto = new UserDTO();
        userDto.setId(UUID.randomUUID());
        userDto.setUsername("test-" + UUID.randomUUID());
        userDto.setEmail("test-" + UUID.randomUUID() + "@test.com");
        userDto.setRole(role);
        userDto.setCreatedAt(new Date());
        userDto.setValid(true);

        when(userAuthClient.current(any())).thenReturn(userDto);
    }

}
