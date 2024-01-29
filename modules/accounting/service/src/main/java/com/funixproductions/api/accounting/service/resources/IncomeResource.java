package com.funixproductions.api.accounting.service.resources;

import com.funixproductions.api.accounting.client.clients.IncomeClient;
import com.funixproductions.api.accounting.client.dtos.IncomeDTO;
import com.funixproductions.api.accounting.service.services.IncomeCrudService;
import com.funixproductions.core.crud.resources.ApiResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounting/income")
public class IncomeResource extends ApiResource<IncomeDTO, IncomeCrudService> implements IncomeClient {
    public IncomeResource(IncomeCrudService incomeCrudService) {
        super(incomeCrudService);
    }
}
