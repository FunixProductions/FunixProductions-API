package com.funixproductions.api.payment.paypal.service.webhooks.resources;

import com.funixproductions.api.payment.paypal.service.config.PaypalConfig;
import com.funixproductions.api.payment.paypal.service.webhooks.services.PaypalWebhookService;
import com.funixproductions.api.payment.paypal.service.webhooks.services.SubscriptionsWebhookPaypalService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;
import java.util.zip.CRC32;

@RestController
@RequestMapping("/paypal/webhooks")
@Slf4j(topic = "PaypalWebhookResource")
public class PaypalWebhookResource {

    private static final String PAYPAL_CERT_URL = "https://api.paypal.com";
    private final Iterable<PaypalWebhookService> webhookServices;
    private final String webhookId;

    public PaypalWebhookResource(
            PaypalConfig paypalConfig,
            SubscriptionsWebhookPaypalService saleCompleteWebhookService
    ) {
        this.webhookId = paypalConfig.getWebhookId();
        this.webhookServices = List.of(
                saleCompleteWebhookService
        );
    }

    @PostMapping("cb")
    public ResponseEntity<String> handleWebhook(@RequestBody String event, @RequestHeader HttpHeaders headers) {
        if (!this.verifySignature(event, headers)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        final JsonObject payload = JsonParser.parseString(event).getAsJsonObject();
        final String eventType = payload.get("event_type").getAsString();
        final JsonObject ressource = payload.getAsJsonObject("resource");

        for (PaypalWebhookService webhookService : webhookServices) {
            if (webhookService.getEventType().equals(eventType)) {
                webhookService.handleWebhookEvent(ressource);
            }
        }

        return ResponseEntity.ok().build();
    }

    private boolean verifySignature(String event, HttpHeaders headers) {
        try {
            final String transmissionId = headers.getFirst("paypal-transmission-id");
            final String timestamp = headers.getFirst("paypal-transmission-time");
            final String signature = headers.getFirst("paypal-transmission-sig");
            final String certUrl = headers.getFirst("paypal-cert-url");

            if (!isValidCertUrl(certUrl)) {
                return false;
            }

            final int crc = crc32(event);
            final String message = transmissionId + "|" + timestamp + "|" + webhookId + "|" + crc;

            final String certPem = downloadCert(certUrl);
            final PublicKey publicKey = getPublicKeyFromPem(certPem);

            return verifySignatureWithPublicKey(message, signature, publicKey);
        } catch (Exception e) {
            log.error("Error verifying signature: {}", e.getMessage(), e);
            return false;
        }
    }

    private int crc32(String input) {
        CRC32 crc = new CRC32();

        crc.update(input.getBytes(StandardCharsets.UTF_8));
        return (int) crc.getValue();
    }

    private String downloadCert(String certUrl) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(certUrl, String.class);
    }

    private boolean isValidCertUrl(String certUrl) {
        return certUrl != null && certUrl.startsWith(PAYPAL_CERT_URL);
    }

    private PublicKey getPublicKeyFromPem(String pem) throws Exception {
        try {
            String publicKeyPEM = pem.replace("-----BEGIN CERTIFICATE-----", "")
                    .replace("-----END CERTIFICATE-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
            CertificateFactory factory = CertificateFactory.getInstance("X.509");

            try (final ByteArrayInputStream bais = new ByteArrayInputStream(decoded)) {
                X509Certificate certificate = (X509Certificate) factory.generateCertificate(bais);
                return certificate.getPublicKey();
            }
        } catch (Exception e) {
            log.error("Error parsing public keyPem: {}", pem, e);
            throw e;
        }
    }

    private boolean verifySignatureWithPublicKey(String message, String signature, PublicKey publicKey) throws Exception {
        final byte[] signatureBytes = Base64.getDecoder().decode(signature);
        final Signature sig = Signature.getInstance("SHA256withRSA");

        sig.initVerify(publicKey);
        sig.update(message.getBytes(StandardCharsets.UTF_8));
        return sig.verify(signatureBytes);
    }

}
