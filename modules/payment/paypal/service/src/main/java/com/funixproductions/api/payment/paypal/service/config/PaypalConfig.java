package com.funixproductions.api.payment.paypal.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("paypal")
public class PaypalConfig {

    /**
     * Application client id got in developer dashboard
     */
    private String clientId;

    /**
     * Application client secret got in developer dashboard
     */
    private String clientSecret;

    /**
     * Domain name, with protocol. Sandbox or live
     */
    private String paypalDomain;

    /**
     * Paypal email merchant email
     */
    private String paypalOwnerEmail;

    /**
     * PayPal webhook id
     */
    private String webhookId;

}
