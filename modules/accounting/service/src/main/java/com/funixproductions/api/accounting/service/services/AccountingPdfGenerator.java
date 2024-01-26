package com.funixproductions.api.accounting.service.services;

import com.funixproductions.api.accounting.service.entities.AccountingReport;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.tools.pdf.entities.PDFLine;
import com.funixproductions.core.tools.pdf.generators.PDFGeneratorWithHeaderAndFooter;

import java.awt.*;
import java.io.File;
import java.util.List;

public class AccountingPdfGenerator extends PDFGeneratorWithHeaderAndFooter {

    private final AccountingReport accountingReport;
    private final String dateReport;

    protected AccountingPdfGenerator(final AccountingReport accountingReport,
                                     final String dateReport) {
        super("funix-prod-accounting", null);
        this.accountingReport = accountingReport;
        this.dateReport = dateReport;
    }

    public File generate() throws ApiException {
        super.newPage();

        super.writePlainText(List.of(
                new PDFLine(
                        "Revenus à déclarer à L'URSAFF",
                        DEFAULT_FONT_SIZE + 3,
                        DEFAULT_FONT,
                        Color.BLACK
                ),
                new PDFLine(
                        "Revenus HT totaux : " + parseDoubleEuros(accountingReport.getCaHtTotal()),
                        DEFAULT_FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                )
        ));
        super.yPosition -= super.margin * 2;

        super.writePlainText(List.of(
                new PDFLine(
                        "Revenus à déclarer sur Impots.gouv (TVA)",
                        DEFAULT_FONT_SIZE + 3,
                        DEFAULT_FONT,
                        Color.BLACK
                ),
                new PDFLine(
                        "CASE A1 & 08 - Revenus HT de prestations de services en France et hors UE : " + parseDoubleEuros(accountingReport.getCaHtPrestation()),
                        DEFAULT_FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        "CASE A3 - Dépenses HT de prestations de services dans UE : " + parseDoubleEuros(accountingReport.getHtServiceBoughtNonEu()),
                        DEFAULT_FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        "CASE A2 - Dépenses HT de prestations de services hors UE : " + parseDoubleEuros(accountingReport.getHtServiceBoughtNonEu()),
                        DEFAULT_FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        "CASE B2 - Dépenses HT de biens physiques dans UE : " + parseDoubleEuros(accountingReport.getHtPhysicalProductBoughtEu()),
                        DEFAULT_FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        "CASE A4 - Dépenses HT de biens physiques hors UE : " + parseDoubleEuros(accountingReport.getHtPhysicalProductBoughtNonEu()),
                        DEFAULT_FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        "CASE E3 - TVA à déclarer par pays membre de l'union européenne (10k€ de ca annuel) : " + parseDoubleEuros(accountingReport.getHtPrestationSoldEu().values().stream().reduce(0.0, Double::sum)),
                        DEFAULT_FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        "CASE 17 - TVA à déclarer pour les autoliquidation dans l'ue : " + parseDoubleEuros(accountingReport.getIntracomVat()),
                        DEFAULT_FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        "CASE 20 - TVA à déduire des achats : " + parseDoubleEuros(accountingReport.getTvaToDeduct()),
                        DEFAULT_FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                )
        ));
        return super.generatePDF(new File("."));
    }

    @Override
    protected void setupHeader() throws ApiException {
        super.writePlainText(List.of(
                new PDFLine(
                        "Funix Productions rapport de comptabilité",
                        DEFAULT_FONT_SIZE + 5,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        "Repprt du " + dateReport,
                        DEFAULT_FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                )
        ));
        super.yPosition -= super.margin * 2;
    }

    public String parseDoubleEuros(final Double value) {
        return String.format("%.3f", value) + " €";
    }
}
