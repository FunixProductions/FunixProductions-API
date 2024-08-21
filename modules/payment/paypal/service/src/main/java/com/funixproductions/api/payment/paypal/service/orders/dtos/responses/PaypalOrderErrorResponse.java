package com.funixproductions.api.payment.paypal.service.orders.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaypalOrderErrorResponse {

    private ErrorCode name;

    private List<Details> details;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Details {

        private String field;

        private Issue issue;

        private String description;

        @Override
        public String toString() {
            return "Details{" +
                    "field='" + field + '\'' +
                    ", issue=" + issue +
                    ", description='" + description + '\'' +
                    '}';
        }

        @Getter
        @AllArgsConstructor
        public enum Issue {
            INVALID_ACCOUNT_STATUS("Le statut du compte est invalide."),
            INVALID_ARRAY_MAX_ITEMS("Le nombre maximum d'éléments du tableau est invalide."),
            INVALID_ARRAY_MIN_ITEMS("Le nombre minimum d'éléments du tableau est invalide."),
            INVALID_COUNTRY_CODE("Le code de pays est invalide."),
            INVALID_SECURITY_CODE_LENGTH("Le code de sécurité de carte est invalide."),
            INVALID_PARAMETER_SYNTAX("La syntaxe du paramètre est invalide."),
            INVALID_STRING_LENGTH("La longueur de la chaîne est invalide."),
            INVALID_PARAMETER_VALUE("La valeur du paramètre est invalide."),
            MISSING_REQUIRED_PARAMETER("Le paramètre requis est manquant."),
            NOT_SUPPORTED("Champ non supporté."),
            PAYPAL_REQUEST_ID_REQUIRED("L'identifiant de la requête PayPal est requis."),
            MALFORMED_REQUEST_JSON("La requête JSON est mal formée."),
            PERMISSION_DENIED("Autorisation refusée."),
            PERMISSION_DENIED_FOR_DONATION_ITEMS("Autorisation refusée pour les articles de don."),
            MALFORMED_REQUEST("La requête est mal formée."),
            AMOUNT_MISMATCH("Le montant ne correspond pas."),
            BILLING_ADDRESS_INVALID("L'adresse de facturation est invalide."),
            CANNOT_BE_NEGATIVE("Le montant ne peut pas être négatif."),
            CANNOT_BE_ZERO_OR_NEGATIVE("Le montant ne peut pas être nul ou négatif."),
            CARD_EXPIRED("La carte est expirée."),
            CITY_REQUIRED("La ville est requise."),
            DECIMAL_PRECISION("La précision décimale est invalide."),
            DONATION_ITEMS_NOT_SUPPORTED("Les articles de don ne sont pas supportés."),
            DUPLICATE_REFERENCE_ID("L'identifiant de référence est en double."),
            INVALID_CURRENCY_CODE("Le code de devise est invalide."),
            INVALID_PAYER_ID("L'identifiant du payeur est invalide."),
            ITEM_TOTAL_MISMATCH("Le total des articles ne correspond pas."),
            ITEM_TOTAL_REQUIRED("Le total des articles est requis."),
            MAX_VALUE_EXCEEDED("La valeur maximale est dépassée."),
            MISSING_PICKUP_ADDRESS("L'adresse de ramassage est manquante."),
            MULTI_CURRENCY_ORDER("La commande multi-devises n'est pas supportée."),
            MULTIPLE_ITEM_CATEGORIES("Les catégories d'articles multiples ne sont pas supportées."),
            MULTIPLE_SHIPPING_ADDRESS_NOT_SUPPORTED("Les adresses de livraison multiples ne sont pas supportées."),
            MULTIPLE_SHIPPING_TYPE_NOT_SUPPORTED("Les types de livraison multiples ne sont pas supportés."),
            PAYEE_ACCOUNT_INVALID("Le compte du bénéficiaire est invalide."),
            PAYEE_ACCOUNT_LOCKED_OR_CLOSED("Le compte du bénéficiaire est verrouillé ou fermé."),
            PAYEE_ACCOUNT_RESTRICTED("Le compte du bénéficiaire est restreint."),
            REFERENCE_ID_REQUIRED("L'identifiant de référence est requis."),
            PAYMENT_SOURCE_CANNOT_BE_USED("La source de paiement ne peut pas être utilisée."),
            PAYMENT_SOURCE_DECLINED_BY_PROCESSOR("La source de paiement a été refusée par le processeur."),
            PAYMENT_SOURCE_INFO_CANNOT_BE_VERIFIED("Les informations de la source de paiement ne peuvent pas être vérifiées."),
            POSTAL_CODE_REQUIRED("Le code postal est requis."),
            SHIPPING_ADDRESS_INVALID("L'adresse de livraison est invalide."),
            TAX_TOTAL_MISMATCH("Le total des taxes ne correspond pas."),
            TAX_TOTAL_REQUIRED("Le total des taxes est requis."),
            UNSUPPORTED_INTENT("L'intention payment n'est pas supportée."),
            UNSUPPORTED_PAYMENT_INSTRUCTION("L'instruction de paiement n'est pas supportée."),
            SHIPPING_TYPE_NOT_SUPPORTED_FOR_CLIENT("Le type de livraison n'est pas supporté pour le client."),
            UNSUPPORTED_SHIPPING_TYPE("Le type de livraison n'est pas supporté."),
            SHIPPING_OPTION_NOT_SELECTED("L'option de livraison n'est pas sélectionnée."),
            SHIPPING_OPTIONS_NOT_SUPPORTED("Les options de livraison ne sont pas supportées."),
            MULTIPLE_SHIPPING_OPTION_SELECTED("Plusieurs options de livraison sont sélectionnées."),
            PREFERRED_SHIPPING_OPTION_AMOUNT_MISMATCH("Le montant de l'option de livraison préférée ne correspond pas."),
            CARD_CLOSED("La carte est fermée par la banque."),
            ORDER_CANNOT_BE_SAVED("La commande ne peut pas être enregistrée."),
            SAVE_ORDER_NOT_SUPPORTED("L'enregistrement de la commande n'est pas supporté."),
            PUI_DUPLICATE_ORDER("La commande PUI est en double."),
            CONSENT_NEEDED("Le consentement du client est requis."),
            INVALID_RESOURCE_ID("L'identifiant de ressource est invalide."),
            AGREEMENT_ALREADY_CANCELLED("L'accord est déjà annulé."),
            BILLING_AGREEMENT_NOT_FOUND("L'accord de facturation est introuvable."),
            COMPLIANCE_VIOLATION("Violation de conformité."),
            DOMESTIC_TRANSACTION_REQUIRED("La transaction nationale est requise."),
            DUPLICATE_INVOICE_ID("L'identifiant de facture est en double."),
            INSTRUMENT_DECLINED("L'instrument de paiement a été refusé."),
            ORDER_NOT_APPROVED("La commande n'est pas approuvée."),
            MAX_NUMBER_OF_PAYMENT_ATTEMPTS_EXCEEDED("Le nombre maximum de tentatives de paiement est dépassé."),
            PAYEE_BLOCKED_TRANSACTION("Le bénéficiaire a bloqué la transaction."),
            PAYER_ACCOUNT_LOCKED_OR_CLOSED("Le compte du payeur est verrouillé ou fermé."),
            PAYER_ACCOUNT_RESTRICTED("Le compte du payeur est restreint."),
            PAYER_CANNOT_PAY("Le payeur ne peut pas payer."),
            TRANSACTION_LIMIT_EXCEEDED("La limite de transaction est dépassée."),
            TRANSACTION_RECEIVING_LIMIT_EXCEEDED("La limite de réception de transaction est dépassée."),
            TRANSACTION_REFUSED("La transaction a été refusée."),
            REDIRECT_PAYER_FOR_ALTERNATE_FUNDING("Rediriger le payeur pour un financement alternatif."),
            ORDER_ALREADY_CAPTURED("La commande a déjà été capturée."),
            TRANSACTION_BLOCKED_BY_PAYEE("La transaction a été bloquée par le bénéficiaire."),
            AUTH_CAPTURE_NOT_ENABLED("L'autorisation et la capture ne sont pas activées."),
            NOT_ENABLED_FOR_CARD_PROCESSING("Non activé pour le traitement des cartes."),
            PAYEE_NOT_ENABLED_FOR_CARD_PROCESSING("Le bénéficiaire n'est pas activé pour le traitement des cartes."),
            INVALID_PICKUP_ADDRESS("L'adresse de ramassage est invalide.");

            private final String frMessage;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum ErrorCode {
        /**
         * An internal server error occurred. A system or application error occurred.
         */
        INTERNAL_SERVER_ERROR("Une erreur interne du serveur Paypal s'est produite."),
        /**
         * Authentication failed due to missing authorization header, or invalid authentication credentials.
         */
        AUTHENTICATION_FAILURE("L'authentification avec Paypal a échoué."),
        /**
         * The request is not well-formed, is syntactically incorrect, or violates schema. For more information, see Validation errors.
         */
        INVALID_REQUEST("La requête vers Paypal est mal formée, syntaxiquement incorrecte ou viole le schéma."),
        /**
         * Authorization failed due to insufficient permissions.
         */
        NOT_AUTHORIZED("L'autorisation avec Paypal a échoué."),
        /**
         * The requested resource does not exist.
         */
        RESOURCE_NOT_FOUND("La ressource Paypal demandée n'existe pas."),
        /**
         * The requested action could not be completed, was semantically incorrect, or failed business validation.
         */
        UNPROCESSABLE_ENTITY("L'action demandée vers Paypal n'a pas pu être complétée, est sémantiquement incorrecte ou a échoué à la validation commerciale.");

        private final String frMessage;
    }

}
