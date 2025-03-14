package com.funixproductions.api.payment.paypal.client.dtos.responses;

import com.funixproductions.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Dto pour créer un abonnement mensuel sur Paypal.
 * <a href="https://developer.paypal.com/docs/api/catalog-products/v1/#products_create">Create Product</a>
 * <a href="https://developer.paypal.com/docs/api/subscriptions/v1/#plans_create">Create plan</a>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaypalPlanDTO extends ApiDTO {

    /**
     * The plan id de paypal
     */
    private String planId;

    /**
     * The subscription name.
     */
    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(min = 1, max = 127, message = "Le nom du produit doit contenir entre 1 et 127 caractères")
    private String name;

    /**
     * The subscription description
     */
    @NotBlank(message = "La description du produit est obligatoire")
    @Size(min = 1, max = 256, message = "La description du produit doit contenir entre 1 et 256 caractères")
    private String description;

    /**
     * The image URL for the product.
     */
    @NotBlank(message = "L'URL de l'image du produit est obligatoire")
    @Size(min = 1, max = 2000, message = "L'URL de l'image du produit doit contenir entre 1 et 2000 caractères")
    private String imageUrl;

    /**
     * The home page URL for the product.
     */
    @NotBlank(message = "L'URL de la page d'accueil du produit est obligatoire")
    @Size(min = 1, max = 2000, message = "L'URL de la page d'accueil du produit doit contenir entre 1 et 2000 caractères")
    private String homeUrl;

    /**
     * The subscription price. HT Hors taxes
     */
    @NotNull(message = "Le prix du produit est obligatoire")
    @Min(value = 1, message = "Le prix du produit doit être supérieur à 1")
    private Double price;

    /**
     * Le nom du projet auquel est associé le plan, exemple pacifista pour un pacifista+
     */
    @NotNull(message = "Le nom du projet est obligatoire")
    @Size(min = 1, max = 50, message = "Le nom du projet doit contenir entre 1 et 50 caractères")
    private String projectName;

    public PaypalPlanDTO(String name, String description, String imageUrl, String homeUrl, Double price) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.homeUrl = homeUrl;
        this.price = price;
    }

}
