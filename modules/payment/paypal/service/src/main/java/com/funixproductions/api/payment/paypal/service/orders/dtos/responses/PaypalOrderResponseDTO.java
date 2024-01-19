package com.funixproductions.api.payment.paypal.service.orders.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.funixproductions.api.payment.paypal.client.enums.OrderStatus;
import com.funixproductions.api.payment.paypal.service.orders.dtos.PurchaseUnitDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.requests.PaypalOrderCreationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

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
     * The date and time when the transaction was last updated, in Internet date and time format.
     */
    @JsonProperty(value = "update_time")
    private String updateTime;

    /**
     * The ID of the order.
     */
    private String id;

    /**
     * use the 'approve' type link to redirect
     */
    private List<Link> links;

    /**
     * The payment source used to fund the payment.
     */
    @JsonProperty(value = "payment_source")
    private PaypalOrderCreationDTO.PaymentSource paymentSource;

    /**
     * An array of purchase units. Each purchase unit establishes a contract between a customer and merchant. Each purchase unit represents either a full or partial order that the customer intends to purchase from the merchant.
     */
    @JsonProperty(value = "purchase_units")
    private List<PurchaseUnitDTO> purchaseUnits;

    /**
     * The order status.
     */
    private OrderStatus status;

    @Nullable
    public String getApproveLink() {
        return links.stream()
                .filter(Link::isApproveLink)
                .findFirst()
                .map(Link::getHref)
                .orElse(null);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Link {
        /**
         * The complete target URL. To make the related call, combine the method with this <a href="https://tools.ietf.org/html/rfc6570">URI Template-formatted</a> link. For pre-processing, include the $, (, and ) characters. The href is the key HATEOAS component that links a completed call with a subsequent call.
         */
        private String href;

        /**
         * The link relation type, which serves as an ID for a link that unambiguously describes the semantics of the link. See Link Relations.
         */
        private String rel;

        private String method;

        public boolean isApproveLink() {
            return "approve".equals(rel);
        }
    }

}
