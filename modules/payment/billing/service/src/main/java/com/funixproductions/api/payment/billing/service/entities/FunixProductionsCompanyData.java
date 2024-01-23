package com.funixproductions.api.payment.billing.service.entities;

import com.funixproductions.core.tools.pdf.entities.PDFCompanyData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FunixProductionsCompanyData implements PDFCompanyData {

    private final String name = "FunixProductions";
    private final String zipCode = "67000";
    private final String city = "Strasbourg";
    private final String email = "contact@funixproductions.com";
    private final String website = "https://pacifista.fr";
    private final String siret = "88458906000034";
    private final String tvaCode = "FR 64884589060";

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public String getPhone() {
        return null;
    }
}
