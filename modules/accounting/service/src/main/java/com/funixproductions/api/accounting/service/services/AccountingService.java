package com.funixproductions.api.accounting.service.services;

import com.funixproductions.api.accounting.service.entities.AccountingReport;
import com.funixproductions.api.accounting.service.entities.Income;
import com.funixproductions.api.accounting.service.entities.Product;
import com.funixproductions.api.accounting.service.repositories.IncomeRepository;
import com.funixproductions.api.accounting.service.repositories.ProductRepository;
import com.funixproductions.api.google.gmail.client.clients.GoogleGmailClient;
import com.funixproductions.api.google.gmail.client.dto.MailDTO;
import com.funixproductions.api.google.gmail.client.dto.MailFileDTO;
import com.funixproductions.api.payment.billing.client.clients.BillingFeignInternalClient;
import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.core.exceptions.ApiException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountingService {

    private static final String OWNER_EMAIL = "contact@funixproductions.com";
    private static final String SUBJECT = "[Funix Productions] Rapport mensuel de facturation de %s.";
    private static final String BODY = "Bonjour,<br/><br/>"
            + "Veuillez trouver ci-joint le rapport mensuel de facturation de %s.<br/><br/>"
            + "Cordialement,<br/><br/>"
            + "L'équipe Funix Productions";
    private static final Locale LOCALE = new Locale("fr", "FR");
    private static final ZoneId ZONE_ID = ZoneId.of("Europe/Paris");

    private final BillingFeignInternalClient billingClient;
    private final ProductRepository productRepository;
    private final IncomeRepository incomeRepository;
    private final GoogleGmailClient gmailClient;

    @Scheduled(cron = "0 0 6 3 * *")
    public void sendLastMonthBillingReport() {
        try {
            final Instant[] dateRange = getDateRange();
            final List<BillingDTO> billingData = fetchBillingData();
            final List<Product> products = productRepository.findAllByCreatedAtBetweenOrMonthlyIsTrue(
                    Date.from(dateRange[0]),
                    Date.from(dateRange[1])
            );
            final List<Income> incomes = incomeRepository.findAllByCreatedAtBetween(
                    Date.from(dateRange[0]),
                    Date.from(dateRange[1])
            );

            final AccountingReport accountingReport = new AccountingReport(billingData, products, incomes);
            sendMail(accountingReport);
        } catch (Exception e) {
            log.error("Error while sending last month billing report", e);
            throw new ApiException("Error while sending last month billing report", e);
        }
    }

    private void sendMail(final AccountingReport accountingReport) {
        final String monthAndYearString = getMonthAndYearString();
        final String subject = String.format(SUBJECT, monthAndYearString);
        final String body = String.format(BODY, monthAndYearString);

        try (final AccountingPdfGenerator accountingPdfGenerator = new AccountingPdfGenerator(accountingReport, monthAndYearString)) {
            final File pdfFile = accountingPdfGenerator.generate();

            gmailClient.sendMail(
                    new MailDTO(
                            subject,
                            body,
                            new MailFileDTO(pdfFile)
                    ),
                    new String[]{OWNER_EMAIL}
            );
            if (!pdfFile.delete()) {
                log.warn("Unable to delete file {}", pdfFile.getAbsolutePath());
            }
        } catch (FeignException e) {
            throw new ApiException("Error while sending mail", e);
        }
    }

    private String getMonthAndYearString() {
        final LocalDate currentDate = LocalDate.now();
        final String frenchMonth = currentDate.getMonth().getDisplayName(
                TextStyle.FULL,
                LOCALE
        );
        final int year = currentDate.getYear();

        return frenchMonth + " " + year;
    }

    private Instant[] getDateRange() {
        final LocalDate date = LocalDate.now().minusMonths(1);
        final LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        final LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());

        return new Instant[] {
                firstDayOfMonth.atStartOfDay(ZONE_ID).toInstant(),
                lastDayOfMonth.atStartOfDay(ZONE_ID).plusDays(1).toInstant()
        };
    }

    private List<BillingDTO> fetchBillingData() {
        try {
            return billingClient.getMonthlyReport(
                    Instant.now().atZone(ZONE_ID).toLocalDate().minusMonths(1)
            );
        } catch (Exception e) {
            throw new ApiException("Error while fetching billing data", e);
        }
    }


}
