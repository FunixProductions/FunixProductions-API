package com.funixproductions.api.payment.billing.client.clients;

import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.core.crud.clients.CrudClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface BillingInternalClient extends CrudClient<BillingDTO> {

    @GetMapping("monthly-report")
    List<BillingDTO> getMonthlyReport(@RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate);

}
