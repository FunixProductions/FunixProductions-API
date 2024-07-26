package com.funixproductions.api.payment.paypal.service.orders.resources;

import com.funixproductions.api.payment.paypal.client.clients.PaypalOrderClient;
import com.funixproductions.api.payment.paypal.client.dtos.requests.PaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.requests.card.CreditCardPaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.requests.paypal.PaypalPaymentDTO;
import com.funixproductions.api.payment.paypal.client.dtos.responses.PaypalOrderDTO;
import com.funixproductions.api.payment.paypal.client.enums.OrderStatus;
import com.funixproductions.api.payment.paypal.service.config.PaypalConfig;
import com.funixproductions.api.payment.paypal.service.orders.dtos.PurchaseUnitDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.requests.PaypalOrderCreationDTO;
import com.funixproductions.api.payment.paypal.service.orders.dtos.responses.PaypalOrderResponseDTO;
import com.funixproductions.api.payment.paypal.service.orders.entities.OrderDTO;
import com.funixproductions.api.payment.paypal.service.orders.services.InvoiceSenderService;
import com.funixproductions.api.payment.paypal.service.orders.services.PaypalOrderCrudService;
import com.funixproductions.api.payment.paypal.service.orders.services.PaypalOrderService;
import com.funixproductions.core.exceptions.ApiException;
import com.funixproductions.core.tools.pdf.tools.VATInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/paypal/orders")
@RequiredArgsConstructor
public class PaypalOrderResource implements PaypalOrderClient {

    private final PaypalOrderService paypalOrderService;
    private final PaypalOrderCrudService paypalOrderCrudService;
    private final PaypalConfig paypalConfig;

    private final InvoiceSenderService invoiceSenderService;

    @Override
    public PaypalOrderDTO createCardOrder(CreditCardPaymentDTO creditCardPaymentDTO) {
        try {
            OrderDTO orderDTO = createOrderFromRequest(creditCardPaymentDTO);
            final PaypalOrderResponseDTO response = paypalOrderService.createOrder(orderDTO.getId().toString(), createCardOrderDTO(creditCardPaymentDTO));

            orderDTO.setOrderId(response.getId());
            if (response.getStatus() == OrderStatus.COMPLETED) {
                orderDTO.setPaid(true);
            }

            orderDTO = this.paypalOrderCrudService.update(orderDTO);

            if (orderDTO.getPaid()) {
                this.invoiceSenderService.sendInvoice(response, orderDTO, creditCardPaymentDTO);
            }
            log.info("Order credit card created: from: {} user: {} order id: {}", creditCardPaymentDTO.getOriginRequest(), creditCardPaymentDTO.getUser().toString(), response.getId());
            return mapPaypalResponse(response, orderDTO);
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception e) {
            log.error("Erreur interne lors de la création du paiement par carte.", e);
            throw new ApiException("Erreur interne lors de la création du paiement par carte.", e);
        }
    }

    @Override
    public PaypalOrderDTO createPaypalOrder(PaypalPaymentDTO paypalPaymentDTO) {
        try {
            OrderDTO orderDTO = createOrderFromRequest(paypalPaymentDTO);
            final PaypalOrderResponseDTO response = paypalOrderService.createOrder(orderDTO.getId().toString(), createPaypalOrderDTO(paypalPaymentDTO));

            orderDTO.setOrderId(response.getId());
            orderDTO = this.paypalOrderCrudService.update(orderDTO);

            log.info("Order paypal created: from: {} user: {} order id: {}", paypalPaymentDTO.getOriginRequest(), paypalPaymentDTO.getUser().toString(), response.getId());
            return mapPaypalResponse(response, orderDTO);
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception e) {
            log.error("Erreur interne lors de la création du paiement via paypal.", e);
            throw new ApiException("Erreur interne lors de la création du paiement via paypal.", e);
        }
    }

    @Override
    public PaypalOrderDTO getOrder(String orderId) {
        try {
            final OrderDTO orderDTO = this.paypalOrderCrudService.findByOrderId(orderId);
            final PaypalOrderResponseDTO response = paypalOrderService.getOrder(orderId);

            return mapPaypalResponse(response, orderDTO);
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception e) {
            log.error("Erreur interne lors de la récupération du paiement.", e);
            throw new ApiException("Erreur interne lors de la récupération du paiement.", e);
        }
    }

    @Override
    public PaypalOrderDTO captureOrder(String orderId) {
        try {
            OrderDTO orderDTO = this.paypalOrderCrudService.findByOrderId(orderId);
            final PaypalOrderResponseDTO response = paypalOrderService.captureOrder(orderDTO.getId().toString(), orderId);

            if (response.getStatus() == OrderStatus.COMPLETED) {
                orderDTO.setPaid(true);
                orderDTO = this.paypalOrderCrudService.update(orderDTO);
                this.invoiceSenderService.sendInvoice(response, orderDTO, null);
            }

            log.info("Order capture sended: order id: {} status: {}", response.getId(), response.getStatus());
            return mapPaypalResponse(response, orderDTO);
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception e) {
            log.error("Erreur interne lors de la capture du paiement.", e);
            throw new ApiException("Erreur interne lors de la capture du paiement.", e);
        }
    }

    private OrderDTO createOrderFromRequest(PaymentDTO paymentDTO) {
        final OrderDTO orderDTO = new OrderDTO();

        if (paymentDTO.getUser() != null) {
            final PaymentDTO.UserPaymentDTO userPaymentDTO = paymentDTO.getUser();

            orderDTO.setUserId(userPaymentDTO.getUserId() == null ? null : userPaymentDTO.getUserId().toString());
            orderDTO.setUserEmail(userPaymentDTO.getUserEmail());
            orderDTO.setUsername(userPaymentDTO.getUsername());
        }
        orderDTO.setVatInformation(paymentDTO.getVatInformation());
        orderDTO.setOriginRequest(paymentDTO.getOriginRequest());
        orderDTO.setPaid(false);
        orderDTO.setCardPayment(paymentDTO instanceof CreditCardPaymentDTO);

        return this.paypalOrderCrudService.create(orderDTO);
    }

    private PaypalOrderDTO mapPaypalResponse(final PaypalOrderResponseDTO responseDTO, final OrderDTO orderDTO) {
        final List<PaymentDTO.PurchaseUnitDTO> purchaseUnitDTOS = new ArrayList<>();

        for (final PurchaseUnitDTO purchaseUnitDTO : responseDTO.getPurchaseUnits()) {
            final PaymentDTO.PurchaseUnitDTO toAdd = new PaymentDTO.PurchaseUnitDTO();

            toAdd.setCustomId(purchaseUnitDTO.getCustomId());
            toAdd.setDescription(purchaseUnitDTO.getDescription());
            toAdd.setReferenceId(purchaseUnitDTO.getReferenceId());
            toAdd.setSoftDescriptor(purchaseUnitDTO.getSoftDescriptor());
            toAdd.setItems(new ArrayList<>());

            if (purchaseUnitDTO.getItems() == null) {
                continue;
            }
            for (final PurchaseUnitDTO.Item item : purchaseUnitDTO.getItems()) {
                final PaymentDTO.PurchaseUnitDTO.Item itemToAdd = new PaymentDTO.PurchaseUnitDTO.Item();

                itemToAdd.setName(item.getName());
                itemToAdd.setQuantity(Integer.parseInt(item.getQuantity()));
                itemToAdd.setDescription(item.getDescription());
                itemToAdd.setPrice(Double.parseDouble(item.getUnitAmount().getValue()));
                itemToAdd.setTax(item.getTax() == null ? 0 : Double.parseDouble(item.getTax().getValue()));

                toAdd.getItems().add(itemToAdd);
            }

            purchaseUnitDTOS.add(toAdd);
        }

        return new PaypalOrderDTO(
                responseDTO.getId(),
                responseDTO.getCreateTime(),
                responseDTO.getUpdateTime(),
                responseDTO.getPaymentSource() == null || responseDTO.getPaymentSource().getCard() != null,
                responseDTO.getStatus(),
                purchaseUnitDTOS,
                orderDTO.getVatInformation(),
                responseDTO.getApproveLink() == null ? responseDTO.getPayerActionLink() : responseDTO.getApproveLink()
        );
    }

    private PaypalOrderCreationDTO createPaypalOrderDTO(final PaypalPaymentDTO paymentDTO) {
        final PaymentDTO.BillingAddressDTO billingAddressDTO = paymentDTO.getBillingAddress();
        final PaypalOrderCreationDTO orderDTO = createBaseOrderDTO(paymentDTO.getPurchaseUnits(), paymentDTO.getVatInformation());
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
        final PaypalOrderCreationDTO orderDTO = createBaseOrderDTO(paymentDTO.getPurchaseUnits(), paymentDTO.getVatInformation());
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

    private PaypalOrderCreationDTO createBaseOrderDTO(final List<PaymentDTO.PurchaseUnitDTO> purchaseUnits, @Nullable VATInformation vatInformation) {
        final PaypalOrderCreationDTO orderDTO = new PaypalOrderCreationDTO();

        orderDTO.setIntent(PaypalOrderCreationDTO.PaypalIntent.CAPTURE);
        orderDTO.setPurchaseUnits(createPurchaseList(purchaseUnits, vatInformation));
        return orderDTO;
    }

    private List<PurchaseUnitDTO> createPurchaseList(final List<PaymentDTO.PurchaseUnitDTO> purchaseUnits, @Nullable VATInformation vatInformation) {
        final List<PurchaseUnitDTO> toSend = new ArrayList<>();

        for (final PaymentDTO.PurchaseUnitDTO purchase : purchaseUnits) {
            toSend.add(new PurchaseUnitDTO(
                    initPurchaseAmount(purchase, vatInformation),
                    purchase.getCustomId(),
                    purchase.getDescription(),
                    generateItems(purchase.getItems(), vatInformation),
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

    private List<PurchaseUnitDTO.Item> generateItems(final List<PaymentDTO.PurchaseUnitDTO.Item> items, @Nullable VATInformation vatInformation) {
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
                    vatInformation == null ? null : new PurchaseUnitDTO.Money(
                            "EUR",
                            parseDoubleToString(item.getPrice() * (vatInformation.getVatRate() / 100))
                    ),
                    PurchaseUnitDTO.Category.DIGITAL_GOODS
            ));
        }
        return toSend;
    }

    private PurchaseUnitDTO.Amount initPurchaseAmount(final PaymentDTO.PurchaseUnitDTO purchaseUnitDTO, @Nullable VATInformation vatInformation) {
        final PurchaseUnitDTO.Amount amount = new PurchaseUnitDTO.Amount();
        double totalHt = 0;

        for (final PaymentDTO.PurchaseUnitDTO.Item item : purchaseUnitDTO.getItems()) {
            totalHt += item.getPrice() * item.getQuantity();
        }

        final double totalTaxes = vatInformation == null ? 0 : totalHt * (vatInformation.getVatRate() / 100);
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
        final BigDecimal bd = BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
        final DecimalFormat df = new DecimalFormat("#.00");

        return df.format(bd).replace(",", ".");
    }

    private static String formatCreditCardExpiry(int year, int month) {
        final LocalDate expiryDate = LocalDate.of(year, month, 1);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        return expiryDate.format(formatter);
    }
}
