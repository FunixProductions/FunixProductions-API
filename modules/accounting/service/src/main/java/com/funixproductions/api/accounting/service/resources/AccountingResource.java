package com.funixproductions.api.accounting.service.resources;

import com.funixproductions.api.accounting.service.services.AccountingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounting")
@RequiredArgsConstructor
public class AccountingResource {

    private final AccountingService accountingService;

    @PostMapping("/send-last-month-billing-report")
    public void sendLastMonthBillingReport() {
        accountingService.sendLastMonthBillingReport();
    }

}
