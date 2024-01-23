package com.funixproductions.api.payment.billing.service.resources;

import com.funixproductions.api.payment.billing.client.clients.BillingClient;
import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.service.services.BillingCrudService;
import com.funixproductions.core.crud.dtos.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billing/admin")
@RequiredArgsConstructor
public class BillingAdminResource implements BillingClient {

    private final BillingCrudService billingCrudService;

    @Override
    public PageDTO<BillingDTO> getAll(String page, String elemsPerPage, String search, String sort) {
        return billingCrudService.getAll(page, elemsPerPage, search, sort);
    }

    @Override
    public BillingDTO findById(String id) {
        return billingCrudService.findById(id);
    }

    @Override
    public Resource downloadInvoice(String id) {
        return billingCrudService.getInvoiceFile(id);
    }
}
