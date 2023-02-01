package com.kursaha.mailkeets.dto;

import lombok.Data;
import lombok.NonNull;

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
}
