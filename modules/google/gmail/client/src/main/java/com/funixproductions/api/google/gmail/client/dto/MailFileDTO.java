package com.funixproductions.api.google.gmail.client.dto;

import com.funixproductions.core.exceptions.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailFileDTO {

    /**
     * Add local file and send it
     * @param file file to send
     */
    public MailFileDTO(final File file) {
        this.name = file.getName();
        this.mimeType = determineMimeType(file);
        this.fileContent = readFileContent(file);
    }

    private String name;

    private String mimeType;

    private byte[] fileContent;

    private String determineMimeType(File file) {
        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            throw new ApiException("Impossible de déterminer le type MIME du fichier : " + file.getName(), e);
        }
    }

    private byte[] readFileContent(File file) throws ApiException {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new ApiException("Impossible de lire le contenu du fichier : " + file.getName(), e);
        }
    }

}
