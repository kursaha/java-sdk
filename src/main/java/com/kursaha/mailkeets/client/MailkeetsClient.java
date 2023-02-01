package com.kursaha.mailkeets.client;

import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.dto.MailResponseDto;
import com.kursaha.mailkeets.exception.MailkeetsSendMailException;

/**
 * Mailkeets client
 */
public interface MailkeetsClient {

    /**
     * request to send mail
     * @param subject of email
     * @param to recipient mail address (mandatory field)
     * @param from your mail address (mandatory field)
     * @param fromName your name (mandatory field)
     * @param body of email (mandatory field)
     * @return if successful then you receive a trace-ID else exception occurs
     * @throws MailkeetsSendMailException throws exception
     */
    MailResponseDto send (
            String subject,
            String to,
            String from,
            String fromName,
            String body
    ) throws MailkeetsSendMailException;

    /**
     * request to send mail
     * @param requestDto requires dto to send email
     * @return if successful then you receive a trace-ID else exception occurs
     * @throws MailkeetsSendMailException throws exception
     */
    MailResponseDto sendMail(MailRequestDto requestDto) throws MailkeetsSendMailException;
}
