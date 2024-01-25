package com.funixproductions.api.payment.billing.service.resources;

import com.funixproductions.api.payment.billing.client.clients.BillingInternalClient;
import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.service.services.BillingCrudService;
import com.funixproductions.core.crud.resources.ApiResource;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kubeinternal/billing")
public class BillingInternalResource extends ApiResource<BillingDTO, BillingCrudService> implements BillingInternalClient {
    public BillingInternalResource(BillingCrudService service) {
        super(service);
    }

    @Override
    public List<BillingDTO> create(List<@Valid BillingDTO> request) {
        throw new ApiBadRequestException("Method not allowed for multiple objects");
    }

    @Override
    public BillingDTO update(BillingDTO request) {
        throw new ApiBadRequestException("Method not allowed");
    }

    @Override
    public List<BillingDTO> update(List<BillingDTO> request) {
        throw new ApiBadRequestException("Method not allowed");
    }

    @Override
    public BillingDTO updatePut(BillingDTO request) {
        throw new ApiBadRequestException("Method not allowed");
    }

    @Override
    public List<BillingDTO> updatePut(List<@Valid BillingDTO> request) {
        throw new ApiBadRequestException("Method not allowed");
    }

    @Override
    public void delete(String id) {
        throw new ApiBadRequestException("Method not allowed");
    }

    @Override
    public void delete(String... ids) {
        throw new ApiBadRequestException("Method not allowed");
    }
}
