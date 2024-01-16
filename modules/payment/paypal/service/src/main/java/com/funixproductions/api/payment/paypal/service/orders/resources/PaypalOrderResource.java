package com.funixproductions.api.payment.paypal.service.orders.resources;

import com.funixproductions.api.payment.paypal.client.clients.PaypalOrderClient;
import com.funixproductions.api.payment.paypal.client.dtos.requests.PaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.requests.card.CreditCardPaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.requests.paypal.PaypalPaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalOrderDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.PurchaseUnitDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.requests.PaypalOrderCreationDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.responses.PaypalOrderResponseDTO;
import com.funixproductions.api.payment.paypal.service.orders.entities.OrderDTO;
import com.funixproductions.api.payment.paypal.service.orders.services.PaypalOrderCrudService;
import com.funixproductions.api.payment.paypal.service.orders.services.PaypalOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/paypal/orders")
@RequiredArgsConstructor
public class PaypalOrderResource implements PaypalOrderClient {

    private final PaypalOrderService paypalOrderService;
    private final PaypalOrderCrudService paypalOrderCrudService;

    @Override
    public PaypalOrderDTO createCardOrder(CreditCardPaymentDTO creditCardPaymentDTO) {
        final OrderDTO orderDTO = createOrderFromRequest(creditCardPaymentDTO);
        final PaypalOrderResponseDTO response = paypalOrderService.createOrder(orderDTO.getId().toString(), createCardOrderDTO(creditCardPaymentDTO));

        orderDTO.setOrderId(response.getId());
        this.paypalOrderCrudService.update(orderDTO);
    }

    @Override
    public PaypalOrderDTO createPaypalOrder(PaypalPaymentDTO paypalPaymentDTO) {
        final OrderDTO orderDTO = createOrderFromRequest(paypalPaymentDTO);
        final PaypalOrderResponseDTO response = paypalOrderService.createOrder(orderDTO.getId().toString(), createPaypalOrderDTO(paypalPaymentDTO));

        orderDTO.setOrderId(response.getId());
        this.paypalOrderCrudService.update(orderDTO);
    }

    @Override
    public PaypalOrderDTO getOrder(String orderId) {
        final PaypalOrderResponseDTO response = paypalOrderService.getOrder(orderId);
    }

    private PaypalOrderCreationDTO createPaypalOrderDTO(final PaypalPaymentDTO paymentDTO) {
        final PaypalOrderCreationDTO orderDTO = createBaseOrderDTO(paymentDTO.getPurchaseUnits());
        final PaypalOrderCreationDTO.PaymentSource paymentSource = new PaypalOrderCreationDTO.PaymentSource();

        final PaypalOrderCreationDTO.PaymentSource.Paypal paymentSourcePaypal = new PaypalOrderCreationDTO.PaymentSource.Paypal();
        final PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext experienceContext = new PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext();
        experienceContext.setBrandName(paymentDTO.getBrandName());
        experienceContext.setCancelUrl(paymentDTO.getCancelUrl());
        experienceContext.setReturnUrl(paymentDTO.getReturnUrl());
        experienceContext.setLandingPage(PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.LandingPage.LOGIN);
        experienceContext.setLocale("fr-FR");
        experienceContext.setPaymentMethod(new PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.PaymentMethod(
                PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.PaymentMethod.PayeePreferred.IMMEDIATE_PAYMENT_REQUIRED,
                PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.PaymentMethod.StandardEntryClassCode.WEB
        ));
        experienceContext.setShippingPreference(PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.ShippingPreference.NO_SHIPPING);
        paymentSourcePaypal.setExperienceContext(experienceContext);
        experienceContext.setStoredPaymentSource(new PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.StoredPaymentSource(
                PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.StoredPaymentSource.PaymentInitiator.CUSTOMER,
                PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.StoredPaymentSource.PaymentType.ONE_TIME,
                PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.StoredPaymentSource.Usage.DERIVED
        ));
        experienceContext.setUserAction(PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.UserAction.PAY_NOW);
        paymentSourcePaypal.setExperienceContext(experienceContext);

        paymentSource.setPaypal(paymentSourcePaypal);
        orderDTO.setPaymentSource(paymentSource);
        return orderDTO;
    }

    private PaypalOrderCreationDTO createCardOrderDTO(final CreditCardPaymentDTO paymentDTO) {
        final PaypalOrderCreationDTO orderDTO = createBaseOrderDTO(paymentDTO.getPurchaseUnits());
    }

    private OrderDTO createOrderFromRequest(PaymentDTO paymentDTO) {
        final OrderDTO orderDTO = new OrderDTO();

        if (paymentDTO.getUser() != null) {
            final PaymentDTO.UserPaymentDTO userPaymentDTO = paymentDTO.getUser();

            orderDTO.setUserId(userPaymentDTO.getUserId() == null ? null : userPaymentDTO.getUserId().toString());
            orderDTO.setUserEmail(userPaymentDTO.getUserEmail());
            orderDTO.setUsername(userPaymentDTO.getUsername());
        }
        orderDTO.setOriginRequest(paymentDTO.getOriginRequest());
        orderDTO.setPaid(false);
        orderDTO.setCardPayment(paymentDTO instanceof CreditCardPaymentDTO);

        return this.paypalOrderCrudService.create(orderDTO);
    }

    private PaypalOrderCreationDTO createBaseOrderDTO(final List<PaymentDTO.PurchaseUnitDTO> purchaseUnits) {
        final PaypalOrderCreationDTO orderDTO = new PaypalOrderCreationDTO();

        orderDTO.setIntent(PaypalOrderCreationDTO.PaypalIntent.CAPTURE);
        orderDTO.setPurchaseUnits(createPurchaseList(purchaseUnits));
        return orderDTO;
    }

    private List<PurchaseUnitDTO> createPurchaseList(final List<PaymentDTO.PurchaseUnitDTO> purchaseUnits) {

    }
}
