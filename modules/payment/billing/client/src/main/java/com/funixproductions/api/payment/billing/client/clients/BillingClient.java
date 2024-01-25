package com.funixproductions.api.payment.billing.client.clients;

import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.core.crud.dtos.PageDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface BillingClient {

    @GetMapping
    PageDTO<BillingDTO> getAll(@RequestParam(value = "page", defaultValue = "0") String page,
                               @RequestParam(value = "elemsPerPage", defaultValue = "300") String elemsPerPage,
                               @RequestParam(value = "search", defaultValue = "") String search,
                               @RequestParam(value = "sort", defaultValue = "") String sort);

    @GetMapping("{id}")
    BillingDTO findById(@PathVariable("id") String id);

    @GetMapping("{id}/invoice")
    Resource downloadInvoice(@PathVariable String id);

}
