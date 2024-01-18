package com.funixproductions.api.payment.paypal.service.orders.resources;

import com.funixproductions.api.payment.paypal.client.clients.PaypalOrderClient;
import com.funixproductions.api.payment.paypal.client.dtos.requests.PaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.requests.card.CreditCardPaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.requests.paypal.PaypalPaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalOrderDTO;
import com.funixproductions.api.payment.paypal.service.config.PaypalConfig;
import com.funixproductions.api.payment.paypal.service.orders.dtos.PurchaseUnitDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.requests.PaypalOrderCreationDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.responses.PaypalOrderResponseDTO;
import com.funixproductions.api.payment.paypal.service.orders.entities.OrderDTO;
import com.funixproductions.api.payment.paypal.service.orders.services.PaypalOrderCrudService;
import com.funixproductions.api.payment.paypal.service.orders.services.PaypalOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/paypal/orders")
@RequiredArgsConstructor
public class PaypalOrderResource implements PaypalOrderClient {

    private final PaypalOrderService paypalOrderService;
    private final PaypalOrderCrudService paypalOrderCrudService;
    private final PaypalConfig paypalConfig;

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
        final OrderDTO orderDTO = this.paypalOrderCrudService.findByOrderId(orderId);
        final PaypalOrderResponseDTO response = paypalOrderService.getOrder(orderId);
    }

    @Override
    public PaypalOrderDTO captureOrder(String orderId) {
        final OrderDTO orderDTO = this.paypalOrderCrudService.findByOrderId(orderId);
        final PaypalOrderResponseDTO response = paypalOrderService.captureOrder(orderDTO.getId().toString(), orderId);

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

    private PaypalOrderCreationDTO createPaypalOrderDTO(final PaypalPaymentDTO paymentDTO) {
        final PaymentDTO.BillingAddressDTO billingAddressDTO = paymentDTO.getBillingAddress();
        final PaypalOrderCreationDTO orderDTO = createBaseOrderDTO(paymentDTO.getPurchaseUnits());
        final PaypalOrderCreationDTO.PaymentSource paymentSource = new PaypalOrderCreationDTO.PaymentSource();

        final PaypalOrderCreationDTO.PaymentSource.Paypal paymentSourcePaypal = new PaypalOrderCreationDTO.PaymentSource.Paypal(
                new PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext(
                        paymentDTO.getBrandName(),
                        paymentDTO.getCancelUrl(),
                        paymentDTO.getReturnUrl(),
                        PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.LandingPage.LOGIN,
                        "fr-FR",
                        new PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.PaymentMethod(
                                PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.PaymentMethod.PayeePreferred.IMMEDIATE_PAYMENT_REQUIRED,
                                PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.PaymentMethod.StandardEntryClassCode.WEB
                        ),
                        PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.ShippingPreference.NO_SHIPPING,
                        new PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.StoredPaymentSource(
                                PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.StoredPaymentSource.PaymentInitiator.CUSTOMER,
                                PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.StoredPaymentSource.PaymentType.ONE_TIME,
                                PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.StoredPaymentSource.Usage.DERIVED
                        ),
                        PaypalOrderCreationDTO.PaymentSource.Paypal.ExperienceContext.UserAction.PAY_NOW
                ),
                new PaypalOrderCreationDTO.PaymentSource.Paypal.Address(
                        billingAddressDTO.getAddress(),
                        null,
                        billingAddressDTO.getCity(),
                        null,
                        billingAddressDTO.getPostalCode(),
                        billingAddressDTO.getCountryCode()
                )
        );

        paymentSource.setPaypal(paymentSourcePaypal);
        orderDTO.setPaymentSource(paymentSource);
        return orderDTO;
    }

    private PaypalOrderCreationDTO createCardOrderDTO(final CreditCardPaymentDTO paymentDTO) {
        final PaymentDTO.BillingAddressDTO billingAddressDTO = paymentDTO.getBillingAddress();
        final PaypalOrderCreationDTO orderDTO = createBaseOrderDTO(paymentDTO.getPurchaseUnits());
        final PaypalOrderCreationDTO.PaymentSource paymentSource = new PaypalOrderCreationDTO.PaymentSource();

        final PaypalOrderCreationDTO.PaymentSource.Card paymentSourceCard = new PaypalOrderCreationDTO.PaymentSource.Card(
                paymentDTO.getCardHolderName(),
                paymentDTO.getCardNumber(),
                paymentDTO.getSecurityCode(),
                formatCreditCardExpiry(paymentDTO.getExpirationYear(), paymentDTO.getExpirationMonth()),
                new PaypalOrderCreationDTO.PaymentSource.Card.BillingAddress(
                        billingAddressDTO.getAddress(),
                        null,
                        billingAddressDTO.getCity(),
                        null,
                        billingAddressDTO.getPostalCode(),
                        billingAddressDTO.getCountryCode()
                ),
                new PaypalOrderCreationDTO.PaymentSource.Card.StoredCredentials(),
                null,
                new PaypalOrderCreationDTO.PaymentSource.Card.ExperienceContext(
                        paymentDTO.getReturnUrl(),
                        paymentDTO.getCancelUrl()
                )
        );

        paymentSource.setCard(paymentSourceCard);
        orderDTO.setPaymentSource(paymentSource);
        return orderDTO;
    }

    private PaypalOrderCreationDTO createBaseOrderDTO(final List<PaymentDTO.PurchaseUnitDTO> purchaseUnits) {
        final PaypalOrderCreationDTO orderDTO = new PaypalOrderCreationDTO();

        orderDTO.setIntent(PaypalOrderCreationDTO.PaypalIntent.CAPTURE);
        orderDTO.setPurchaseUnits(createPurchaseList(purchaseUnits));
        return orderDTO;
    }

    private List<PurchaseUnitDTO> createPurchaseList(final List<PaymentDTO.PurchaseUnitDTO> purchaseUnits) {
        final List<PurchaseUnitDTO> toSend = new ArrayList<>();

        for (final PaymentDTO.PurchaseUnitDTO purchase : purchaseUnits) {
            toSend.add(new PurchaseUnitDTO(
                    initPurchaseAmount(purchase),
                    purchase.getCustomId(),
                    purchase.getDescription(),
                    generateItems(purchase.getItems()),
                    new PurchaseUnitDTO.Payee(
                            this.paypalConfig.getPaypalOwnerEmail(),
                            null
                    ),
                    purchase.getReferenceId(),
                    purchase.getSoftDescriptor()
            ));
        }

        return toSend;
    }

    private List<PurchaseUnitDTO.Item> generateItems(final List<PaymentDTO.PurchaseUnitDTO.Item> items) {
        final List<PurchaseUnitDTO.Item> toSend = new ArrayList<>();

        for (final PaymentDTO.PurchaseUnitDTO.Item item : items) {
            toSend.add(new PurchaseUnitDTO.Item(
                    item.getName(),
                    Integer.toString(item.getQuantity()),
                    item.getDescription(),
                    new PurchaseUnitDTO.Money(
                            "EUR",
                            parseDoubleToString(item.getPrice())
                    ),
                    item.getVatInformation() == null ? null : new PurchaseUnitDTO.Money(
                            "EUR",
                            parseDoubleToString(item.getPrice() * (item.getVatInformation().getVatRate() / 100))
                    ),
                    PurchaseUnitDTO.Category.DIGITAL_GOODS
            ));
        }
        return toSend;
    }

    private PurchaseUnitDTO.Amount initPurchaseAmount(final PaymentDTO.PurchaseUnitDTO purchaseUnitDTO) {
        final PurchaseUnitDTO.Amount amount = new PurchaseUnitDTO.Amount();
        double totalHt = 0;
        double totalTaxes = 0;

        for (final PaymentDTO.PurchaseUnitDTO.Item item : purchaseUnitDTO.getItems()) {
            totalHt += item.getPrice() * item.getQuantity();

            if (item.getVatInformation() != null) {
                totalTaxes += (item.getPrice() * (item.getVatInformation().getVatRate() / 100)) * item.getQuantity();
            }
        }

        amount.setCurrencyCode("EUR");
        amount.setValue(parseDoubleToString(totalHt + totalTaxes));
        amount.setBreakdown(new PurchaseUnitDTO.Amount.Breakdown(
                new PurchaseUnitDTO.Money(
                        "EUR",
                        parseDoubleToString(totalHt)
                ),
                new PurchaseUnitDTO.Money(
                        "EUR",
                        parseDoubleToString(totalTaxes)
                ),
                null
        ));
        return amount;
    }

    private static String parseDoubleToString(double value) {
        return String.format("%.10f", value);
    }

    private static String formatCreditCardExpiry(int year, int month) {
        final LocalDate expiryDate = LocalDate.of(year, month, 1);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        return expiryDate.format(formatter);
    }
}
