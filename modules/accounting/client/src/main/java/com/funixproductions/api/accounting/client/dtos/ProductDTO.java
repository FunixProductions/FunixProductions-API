package com.funixproductions.api.accounting.client.dtos;

import com.funixproductions.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilisé pour enregistrer ce que paye la funixproductions.
 */
@Getter
@Setter
public class ProductDTO extends ApiDTO {
    @NotNull(message = "Vous devez indiquer le nom du produit.")
    @Size(min = 1, max = 255, message = "Le nom du produit doit contenir entre 1 et 255 caractères.")
    private String productName;

    @NotNull(message = "Vous devez indiquer la description du produit.")
    @Size(min = 1, max = 500, message = "La description du produit doit contenir entre 1 et 500 caractères.")
    private String productDescription;

    @NotNull(message = "Vous devez indiquer si la facture est mensuelle ou non.")
    private Boolean monthly;

    @NotNull(message = "Vous devez indiquer si la facture est européenne ou non.")
    private Boolean isEu;

    @NotNull(message = "Vous devez indiquer si l'element acheté est physique ou non.")
    private Boolean isPhysical;

    @NotNull(message = "Vous devez indiquer le montant HT de la facture.")
    @Min(value = 0, message = "Le montant HT de la facture ne peut pas être négatif.")
    private Double amountHT;

    @NotNull(message = "Vous devez indiquer le montant des TAXES de la facture.")
    @Min(value = 0, message = "Le montant des TAXES de la facture ne peut pas être négatif.")
    private Double amountTax;
}
