package com.funixproductions.api.payment.paypal.service.subscriptions.dtos.requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * <a href="https://developer.paypal.com/docs/api/catalog-products/v1/#products_create">Create product</a>
 */
@Getter
public class CreatePaypalProductRequest {

    private final String name;

    private final String description;

    private final String type;

    private final String category;

    @JsonProperty(value = "home_url")
    private final String homeUrl;

    @JsonProperty(value = "image_url")
    private final String imageUrl;

    public CreatePaypalProductRequest(
            final String name,
            final String description,
            final String homeUrl,
            final String imageUrl) {
        this.name = name;
        this.description = description;
        this.type = "DIGITAL";
        this.category = "DIGITAL_GAMES";
        this.homeUrl = homeUrl;
        this.imageUrl = imageUrl;
    }

}
