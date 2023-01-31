package com.kursaha.mailkeets.client;

import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.dto.MailResponseDto;
import com.kursaha.mailkeets.exception.MailkeetsSendMailException;

public interface MailkeetsClient {
    MailResponseDto send (
            String subject,
            String to,
            String from,
            String fromName,
            String body
    ) throws MailkeetsSendMailException;

    MailResponseDto sendMail(MailRequestDto requestDto) throws MailkeetsSendMailException;
}
