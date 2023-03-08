package com.kursaha.mailkeets.dto;

import lombok.Data;
import lombok.NonNull;

/**
 * MailRequestDto is a class by which we can send request to mail-send api
 */
@Data
public class MailRequestDto {
    @NonNull
    private final String fromName;
    @NonNull
    private final String fromAddress;
    @NonNull
    private final String to;
    @NonNull
    private final String subject;
    @NonNull
    private final String body;
    @NonNull
    private final String requestIdentifier;
}
