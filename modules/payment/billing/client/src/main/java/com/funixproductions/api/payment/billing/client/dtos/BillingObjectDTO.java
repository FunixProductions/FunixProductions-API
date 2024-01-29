package com.funixproductions.api.payment.billing.client.dtos;

import com.funixproductions.core.tools.pdf.entities.InvoiceItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillingObjectDTO implements InvoiceItem, Serializable {

    @NotBlank(message = "Le nom de l'objet facturé est obligatoire")
    @Size(max = 100, message = "Le nom de l'objet facturé ne doit pas dépasser 100 caractères")
    private String name;

    @NotBlank(message = "La description de l'objet facturé est obligatoire")
    @Size(max = 300, message = "La description de l'objet facturé ne doit pas dépasser 300 caractères")
    private String description;

    @NotNull(message = "La quantité de l'objet facturé est obligatoire")
    @Min(value = 1, message = "La quantité de l'objet facturé doit être supérieure à 0")
    private Integer quantity;

    @NotNull(message = "Le prix de l'objet facturé est obligatoire")
    @Min(value = 0, message = "Le prix de l'objet facturé doit être supérieur ou égal à 0")
    private Double price;

    @Override
    public @NonNull String getInvoiceItemName() {
        return this.getName();
    }

    @Override
    public @NonNull String getInvoiceItemDescription() {
        return this.getDescription();
    }

    @Override
    public int getInvoiceItemQuantity() {
        return this.getQuantity();
    }

    @Override
    public double getInvoiceItemPrice() {
        return this.getPrice();
    }
}
