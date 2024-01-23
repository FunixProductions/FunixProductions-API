package com.funixproductions.api.payment.paypal.service.orders.clients;

import com.funixproductions.api.payment.paypal.service.orders.dtos.requests.PaypalOrderCreationDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.responses.PaypalOrderResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

public interface PaypalOrderClient {

    /**
     * Create new order payment
     * @param requestId Contains a unique user-generated ID that the server stores for a period of time. Use this header to enforce idempotency on REST API POST calls. You can make these calls any number of times without concern that the server creates or completes an action on a resource more than once. You can retry calls that fail with network timeouts or the HTTP 500 status code. You can retry calls for as long as the server stores the ID.
     * @param request body
     * @return PaypalOrderResponseDTO response 201 created
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    PaypalOrderResponseDTO createOrder(
            @RequestHeader(name = "PayPal-Request-Id") String requestId,
            @RequestBody PaypalOrderCreationDTO request
    );

    /**
     * Get order details by his id
     * @param orderId order id
     * @return PaypalOrderResponseDTO response 200
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    PaypalOrderResponseDTO getOrder(@PathVariable("id") String orderId);

    /**
     * Use Authorize when you need to hold the funds temporarily, perhaps to confirm inventory or handle other aspects before finalizing the transaction.
     * Authorizes payment for an order. To successfully authorize payment for an order, the buyer must first approve the order or a valid payment_source must be provided in the request. A buyer can approve the order upon being redirected to the rel:approve URL that was returned in the HATEOAS links in the create order response.
     * @param requestId Contains a unique user-generated ID that the server stores for a period of time. Use this header to enforce idempotency on REST API POST calls. You can make these calls any number of times without concern that the server creates or completes an action on a resource more than once. You can retry calls that fail with network timeouts or the HTTP 500 status code. You can retry calls for as long as the server stores the ID.
     * @param orderId order id
     * @return data on order
     */
    @PostMapping(value = "{id}/authorize", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    PaypalOrderResponseDTO authorizeOrder(@RequestHeader(name = "PayPal-Request-Id") String requestId,
                                          @PathVariable("id") String orderId
    );

    /**
     * After authorize, Use Capture when you're ready to process the order immediately and want the funds transferred right away.
     * Captures payment for an order. To successfully capture payment for an order, the buyer must first approve the order or a valid payment_source must be provided in the request. A buyer can approve the order upon being redirected to the rel:approve URL that was returned in the HATEOAS links in the create order response.
     * @param requestId The server stores keys for 6 hours. The API callers can request the times to up to 72 hours by speaking to their Account Manager.
     * @param orderId order id
     * @return data on order
     */
    @PostMapping(value = "{id}/capture", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    PaypalOrderResponseDTO captureOrder(@RequestHeader(name = "PayPal-Request-Id") String requestId,
                                        @PathVariable("id") String orderId
    );

}
