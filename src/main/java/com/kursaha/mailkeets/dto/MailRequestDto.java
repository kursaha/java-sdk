package com.kursaha.mailkeets.dto;

import lombok.Data;

@Data
public class MailRequestDto {
    private String fromName;
    private String fromAddress;
    private String to;
    private String subject;
    private String body;
}
