package com.funixproductions.api.google.gmail.client.dto;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MailFileDTOTest {

    @Test
    void testPlainText() throws Exception {
        final String filename = "test";
        final File file = new File(filename + ".txt");
        final String content = "test content";
        if (!file.createNewFile()) {
            throw new IOException("Can't create file");
        }
        Files.writeString(file.toPath(), content, StandardOpenOption.TRUNCATE_EXISTING);

        final MailFileDTO mailFileDTO = new MailFileDTO(file);

        assertEquals(filename + ".txt", mailFileDTO.getName());
        assertEquals("text/plain", mailFileDTO.getMimeType());
        assertEquals(content, new String(mailFileDTO.getFileContent()));

        if (!file.delete()) {
            throw new IOException("Can't delete file");
        }
    }

    @Test
    void testPDF() throws Exception {
        final String filename = "test";
        final File file = new File(filename + ".pdf");
        final String content = "test content pdf";
        if (!file.createNewFile()) {
            throw new IOException("Can't create file");
        }
        Files.writeString(file.toPath(), content, StandardOpenOption.TRUNCATE_EXISTING);

        final MailFileDTO mailFileDTO = new MailFileDTO(file);

        assertEquals(filename + ".pdf", mailFileDTO.getName());
        assertEquals("application/pdf", mailFileDTO.getMimeType());
        assertEquals(content, new String(mailFileDTO.getFileContent()));

        if (!file.delete()) {
            throw new IOException("Can't delete file");
        }
    }

}
