package com.funixproductions.api.google.gmail.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailDTO {

    private String subject;

    private String bodyText;

    private MailFileDTO fileAttachment;
}
