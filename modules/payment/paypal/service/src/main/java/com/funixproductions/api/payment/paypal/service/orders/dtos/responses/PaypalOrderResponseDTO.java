package com.funixproductions.api.payment.paypal.service.orders.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.funixproductions.api.payment.paypal.service.orders.dtos.PurchaseUnitDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaypalOrderResponseDTO {

    /**
     * The date and time when the transaction occurred, in <a href="https://tools.ietf.org/html/rfc3339#section-5.6">Internet date and time format</a>.
     */
    @JsonProperty(value = "create_time")
    private String createTime;

    /**
     * The ID of the order.
     */
    private String id;

    /**
     * use the 'approve' type link to redirect
     */
    private List<Link> links;

    /**
     * An array of purchase units. Each purchase unit establishes a contract between a customer and merchant. Each purchase unit represents either a full or partial order that the customer intends to purchase from the merchant.
     */
    @JsonProperty(value = "purchase_units")
    private List<PurchaseUnitDTO> purchaseUnits;

    /**
     * The order status.
     */
    private Status status;

    /**
     * The date and time when the transaction was last updated, in Internet date and time format.
     */
    @JsonProperty(value = "update_time")
    private String updateTime;

    @Getter
    @Setter
    public static class Link {
        /**
         * The complete target URL. To make the related call, combine the method with this URI Template-formatted link. For pre-processing, include the $, (, and ) characters. The href is the key HATEOAS component that links a completed call with a subsequent call.
         */
        private String href;

        /**
         * The link relation type, which serves as an ID for a link that unambiguously describes the semantics of the link. See Link Relations.
         */
        private String rel;
    }

    public enum Status {
        /**
         * The order was created with the specified context.
         */
        CREATED,
        /**
         * The order was saved and persisted. The order status continues to be in progress until a capture is made with final_capture = true for all purchase units within the order.
         */
        SAVED,
        /**
         * The customer approved the payment through the PayPal wallet or another form of guest or unbranded payment. For example, a card, bank account, or so on.
         */
        APPROVED,
        /**
         * All purchase units in the order are voided.
         */
        VOIDED,
        /**
         * The payment was authorized or the authorized payment was captured for the order.
         */
        COMPLETED,
        /**
         * The order requires an action from the payer (e.g. 3DS authentication). Redirect the payer to the "rel":"payer-action" HATEOAS link returned as part of the response prior to authorizing or capturing the order.
         */
        PAYER_ACTION_REQUIRED
    }

}
