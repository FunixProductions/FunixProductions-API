package com.funixproductions.api.payment.paypal.client.clients;

import com.funixproductions.api.payment.paypal.client.dtos.requests.card.CreditCardPaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.requests.paypal.PaypalPaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalOrderDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PaypalOrderClient {

    @PostMapping("card")
    PaypalOrderDTO createCardOrder(@RequestBody @Valid CreditCardPaymentDTO creditCardPaymentDTO);

    @PostMapping("paypal")
    PaypalOrderDTO createPaypalOrder(@RequestBody @Valid PaypalPaymentDTO paypalPaymentDTO);

    @GetMapping("{id}")
    PaypalOrderDTO getOrder(@PathVariable("id") String orderId);

    @PostMapping("{id}/capture")
    PaypalOrderDTO captureOrder(@PathVariable("id") String orderId);
}
