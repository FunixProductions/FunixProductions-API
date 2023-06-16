package com.funixproductions.api.service.google.gmail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailDTO {

    private String subject;

    private String bodyText;

    private File attachment;

}
