package com.funixproductions.api.service.google.gmail.services;

import com.funixproductions.api.service.google.gmail.config.GoogleGmailConfig;
import com.funixproductions.api.service.google.gmail.dto.MailDTO;
import com.funixproductions.core.exceptions.ApiException;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@Service
@Profile("!test")
public class GoogleGmailService {

    private final GoogleGmailConfig gmailConfig;
    private final InternetAddress serverEmailAddress;
    private final Gmail gmailService;

    public GoogleGmailService(Gmail gmailService,
                              GoogleGmailConfig gmailConfig) throws ApiException {
        this.gmailService = gmailService;
        this.gmailConfig = gmailConfig;

        try {
            this.serverEmailAddress = new InternetAddress(gmailConfig.getAppEmail());
        } catch (MessagingException e) {
            throw new ApiException("Can't set server email address", e);
        }
    }

    /**
     * Send mail
     * @param mailDTO mail entity
     * @param to receivers mail
     * @throws ApiException when error occurs on mail sending
     */
    public void sendSimpleMail(final MailDTO mailDTO, final String... to) throws ApiException {
        final MimeMessage email = this.createEmail(mailDTO, to);
        Message message = this.createMessage(email);

        try {
            message = gmailService.users().messages().send(this.gmailConfig.getAppEmail(), message).execute();
            log.info("Mail sent successfully. Id: {}. Object: {}. Receivers: {}.", message.getId(), mailDTO.getSubject(), to);
        } catch (IOException e) {
            throw new ApiException(String.format("Erreur lors de l'envoi du mail avec le sdk google. Erreur: %s.", e.getMessage()), e);
        }
    }

    private MimeMessage createEmail(final MailDTO mailDTO, final String... to) throws ApiException {
        final Properties properties = new Properties();
        final Session session = Session.getDefaultInstance(properties, null);
        final MimeMessage email = new MimeMessage(session);

        try {
            email.setFrom(this.serverEmailAddress);
            email.setSubject(mailDTO.getSubject());
            for (final String receiver : to) {
                email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(receiver));
            }

            if (mailDTO.getAttachment() != null) {
                email.setContent(this.createAttachmentFile(mailDTO));
            } else {
                email.setContent(mailDTO.getBodyText(), "text/html");
            }
            return email;
        } catch (Exception e) {
            throw new ApiException(String.format("Erreur lors de la création de l'objet d'envoi de mail. Erreur: %s.", e.getMessage()), e);
        }
    }

    private Message createMessage(final MimeMessage email) {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final Message message = new Message();
            final byte[] bytes = outputStream.toByteArray();
            final String encodedEmail = Base64.encodeBase64URLSafeString(bytes);

            email.writeTo(outputStream);
            message.setRaw(encodedEmail);
            return message;
        } catch (Exception e) {
            throw new ApiException(String.format("Erreur lors du formattage de l'email. Erreur: %s.", e.getMessage()), e);
        }
    }

    private Multipart createAttachmentFile(@NonNull final MailDTO mailDto) throws ApiException {
        final File file = mailDto.getAttachment();
        if (!file.exists()) {
            throw new ApiException(String.format("Le fichier %s n'existe pas.", file.getName()));
        }

        try {
            final Multipart multipart = new MimeMultipart();
            final FileDataSource dataSource = new FileDataSource(file);
            MimeBodyPart mimeBodyPart = new MimeBodyPart();

            mimeBodyPart.setContent(mailDto.getBodyText(), "text/html");
            multipart.addBodyPart(mimeBodyPart);

            mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setDataHandler(new DataHandler(dataSource));
            mimeBodyPart.setFileName(file.getName());
            multipart.addBodyPart(mimeBodyPart);
            return multipart;
        } catch (MessagingException e) {
            throw new ApiException(String.format("Erreur lors de l'ajout du fichier %s en pièce jointe. Erreur: %s.", file.getName(), e.getMessage()), e);
        }
    }

}
