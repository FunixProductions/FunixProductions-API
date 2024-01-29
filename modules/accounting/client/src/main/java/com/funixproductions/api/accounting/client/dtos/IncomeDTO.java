package com.funixproductions.api.accounting.client.dtos;

import com.funixproductions.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilisé pour enregistrer ce que reçois la funixproductions. (paye twitch, youtube, etc...)
 * Revenus BtoB
 */
@Getter
@Setter
public class IncomeDTO extends ApiDTO {

    @NotBlank(message = "Vous devez indiquer le nom de la source de revenu.")
    private String incomeName;

    @NotBlank(message = "Vous devez indiquer la description de la source de revenu.")
    private String incomeDescription;

    @NotNull(message = "Vous devez indiquer le montant de la source de revenu.")
    private Double amount;

}
