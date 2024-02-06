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
    private final float FONT_SIZE = DEFAULT_FONT_SIZE - 3;

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
        super.yPosition -= super.margin;

        super.writePlainText(List.of(
                new PDFLine(
                        "Revenus à déclarer sur Impots.gouv (TVA)",
                        DEFAULT_FONT_SIZE + 3,
                        DEFAULT_FONT,
                        Color.BLACK
                ),
                new PDFLine(
                        "CASE A1 - Revenus HT de prestations de services : ",
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        parseDoubleEuros(accountingReport.getCaHtPrestation()),
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.BLACK
                ),
                new PDFLine(
                        "CASE A3 - Dépenses HT de prestations de services dans UE : ",
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        parseDoubleEuros(accountingReport.getHtServiceBoughtEu()),
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.BLACK
                ),
                new PDFLine(
                        "CASE A2 - Dépenses HT de prestations de services hors UE : ",
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        parseDoubleEuros(accountingReport.getHtServiceBoughtNonEu()),
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.BLACK
                ),
                new PDFLine(
                        "CASE B2 - Dépenses HT de biens physiques dans UE : ",
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        parseDoubleEuros(accountingReport.getHtPhysicalProductBoughtEu()),
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.BLACK
                ),
                new PDFLine(
                        "CASE A4 - Dépenses HT de biens physiques hors UE : ",
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        parseDoubleEuros(accountingReport.getHtPhysicalProductBoughtNonEu()),
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.BLACK
                ),
                new PDFLine(
                        "CASE E3 - TVA à déclarer par pays membre de l'union européenne (10k€ de ca annuel pays euro) : ",
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        parseDoubleEuros(accountingReport.getHtPrestationSoldEu().values().stream().reduce(0.0, Double::sum)),
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.BLACK
                ),
                new PDFLine(
                        "CASE 08 - Opérations réalisées en France en HT : ",
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        parseDoubleEuros(accountingReport.getFrancePrestationTaxHt()),
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.BLACK
                ),
                new PDFLine(
                        "CASE 17 - TVA à déclarer pour les autoliquidation dans l'ue : ",
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        parseDoubleEuros(accountingReport.getIntracomVat()),
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.BLACK
                ),
                new PDFLine(
                        "CASE 20 - TVA à déduire des achats : ",
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.GRAY
                ),
                new PDFLine(
                        parseDoubleEuros(accountingReport.getTvaToDeduct()),
                        FONT_SIZE,
                        DEFAULT_FONT,
                        Color.BLACK
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
                        "Rapport du " + dateReport,
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
