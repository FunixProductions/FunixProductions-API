package com.funixproductions.api.payment.billing.service.services;

import com.funixproductions.api.google.gmail.client.clients.GoogleGmailClient;
import com.funixproductions.api.google.gmail.client.dto.MailDTO;
import com.funixproductions.api.google.gmail.client.dto.MailFileDTO;
import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.service.entities.Billing;
import com.funixproductions.api.payment.billing.service.entities.FunixProductionsCompanyData;
import com.funixproductions.api.payment.billing.service.mappers.BillingMapper;
import com.funixproductions.api.payment.billing.service.repositories.BillingRepository;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.dtos.UserSession;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.api.user.client.security.CurrentSession;
import com.funixproductions.core.crud.services.ApiService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.exceptions.ApiForbiddenException;
import com.funixproductions.core.exceptions.ApiNotFoundException;
import com.funixproductions.core.tools.classpath.ImageReaderClasspath;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BillingCrudService extends ApiService<BillingDTO, Billing, BillingMapper, BillingRepository> {

    private static final ZoneId ZONE_ID = ZoneId.of("Europe/Paris");
    private static final String THANKS_FOR_YOUR_PURCHASE = "Merci pour votre achat ! Vous trouverez ci-joint votre facture. Si vous avez des questions, n'hésitez pas à nous contacter à l'adresse contact@funixproductions.com.";
    private final FunixProductionsCompanyData funixProductionsCompanyData = new FunixProductionsCompanyData();
    private final File pdfDirectory;
    private final byte[] funixProdLogo;

    private final CurrentSession currentSession;
    private final GoogleGmailClient googleGmailClient;

    public BillingCrudService(BillingRepository repository,
                              BillingMapper mapper,
                              CurrentSession currentSession,
                              GoogleGmailClient googleGmailClient,
                              Environment environment) {
        super(repository, mapper);
        this.currentSession = currentSession;
        this.googleGmailClient = googleGmailClient;

        this.pdfDirectory = new File(environment.getProperty("funixproductions.billing.pdf.directory", "invoices"));
        if (!pdfDirectory.exists() && !pdfDirectory.mkdirs()) {
            throw new ApiException("Impossible de créer le dossier des factures");
        }

        final ImageReaderClasspath imageReaderClasspath = new ImageReaderClasspath(BillingCrudService.class, "logos/funixproductions-logo.png", "png");
        this.funixProdLogo = imageReaderClasspath.getBytes();
    }

    public List<BillingDTO> getMonthlyReport(final @NonNull LocalDate date) {
        final Instant[] dateRange = getDateRange(date);
        final List<Billing> billings = super.getRepository().findAllByCreatedAtBetween(Date.from(dateRange[0]), Date.from(dateRange[1]));

        return billings
                .stream()
                .map(super.getMapper()::toDto)
                .toList();
    }

    @Override
    @Transactional
    public @NonNull BillingDTO create(BillingDTO request) {
        final BillingDTO res = super.create(request);

        generatePDF(request, res);
        return res;
    }

    public Resource getInvoiceFile(final @NonNull String id) {
        try {
            final Billing billing = super.getRepository().findByUuid(id).orElseThrow(() -> new ApiNotFoundException("Impossible de récupérer la facture, la facture n'existe pas"));
            if (!canActualUserDownloadInvoice(billing)) {
                throw new ApiForbiddenException("Vous n'avez pas le droit de télécharger cette facture.");
            }

            final Path path = Path.of(billing.getInvoiceFilePath());
            return new ByteArrayResource(Files.readAllBytes(path));
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception e) {
            log.error("Erreur Impossible de récupérer la facture", e);
            throw new ApiException("Impossible de récupérer la facture", e);
        }
    }

    private void generatePDF(final @NonNull BillingDTO request, final @NonNull BillingDTO databaseDTO) {
        if (databaseDTO.getId() == null) {
            throw new ApiException("Impossible de générer la facture, l'identifiant de la facture n'est pas renseigné");
        }
        Billing billing = super.getRepository().findByUuid(databaseDTO.getId().toString()).orElseThrow(() -> new ApiException("Impossible de générer la facture, la facture n'existe pas"));

        try (final InvoiceGenerator invoiceGenerator = new InvoiceGenerator(
                this.funixProductionsCompanyData,
                this.funixProdLogo,
                request.getBillingObjects(),
                request.getBilledEntity(),
                request.getBillingDescription(),
                request.getPaymentType(),
                billing.getId(),
                request.getPaymentOrigin(),
                request.getVatInformation()
        )) {
            invoiceGenerator.init();
            log.info("Génération de la facture id {}", billing.getId());
            final File invoiceFile = invoiceGenerator.generatePDF(this.pdfDirectory);
            log.info("Facture id {} générée", billing.getId());

            billing.setInvoiceFilePath(invoiceFile.getPath());
            billing = super.getRepository().save(billing);

            sendInvoiceByMail(billing, invoiceFile);
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception e) {
            log.error("Impossible de générer la facture", e);
            throw new ApiException("Impossible de générer la facture", e);
        }
    }

    private void sendInvoiceByMail(final Billing billing, final File invoiceFile) {
        try {
            final MailDTO mailDTO = new MailDTO();

            mailDTO.setBodyText(THANKS_FOR_YOUR_PURCHASE);
            mailDTO.setSubject(String.format(
                    "[FunixProductions] Facture N°%d d'achat%s.",
                    billing.getId(),
                    billing.getPaymentOrigin() == null ? "" : " sur " + billing.getPaymentOrigin().getName()
            ));
            mailDTO.setFileAttachment(new MailFileDTO(invoiceFile));

            this.googleGmailClient.sendMail(mailDTO, new String[]{billing.getBilledEntityEmail()});
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception e) {
            log.error("Impossible d'envoyer la facture par mail", e);
            throw new ApiException("Impossible d'envoyer la facture par mail", e);
        }
    }

    private boolean canActualUserDownloadInvoice(final @NonNull Billing billing) {
        final UserSession userSession = this.currentSession.getUserSession();
        if (userSession == null) {
            return false;
        }
        final UserDTO user = userSession.getUserDTO();
        if (user == null || user.getId() == null) {
            return false;
        }

        if (user.getRole().equals(UserRole.ADMIN) ||
                user.getId().toString().equals(billing.getBilledEntityFunixProdId())) {
            return true;
        } else {
            log.info("L'utilisateur id {} nom {} et email {} a essayé de télécharger la facture id {} sans permission.", user.getId(), user.getUsername(), user.getEmail(), billing.getId());
            return false;
        }
    }

    private Instant[] getDateRange(final LocalDate date) {
        final LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        final LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());

        return new Instant[] {
                firstDayOfMonth.atStartOfDay(ZONE_ID).toInstant(),
                lastDayOfMonth.atStartOfDay(ZONE_ID).plusDays(1).toInstant()
        };
    }

    @Override
    public @NonNull BillingDTO update(BillingDTO request) throws ApiNotFoundException, ApiBadRequestException {
        throw new ApiBadRequestException("Impossible de mettre à jour une facture");
    }

    @Override
    public List<BillingDTO> update(List<BillingDTO> request) {
        throw new ApiBadRequestException("Impossible de mettre à jour plusieurs factures à la fois");
    }

    @Override
    public BillingDTO updatePut(BillingDTO request) {
        throw new ApiBadRequestException("Impossible de mettre à jour une facture");
    }

    @Override
    public List<BillingDTO> updatePut(List<@Valid BillingDTO> request) {
        throw new ApiBadRequestException("Impossible de mettre à jour plusieurs factures à la fois");
    }

    @Override
    public void delete(String id) {
        throw new ApiBadRequestException("Impossible de supprimer une facture");
    }

    @Override
    public void delete(String... ids) {
        throw new ApiBadRequestException("Impossible de supprimer plusieurs factures à la fois");
    }
}
