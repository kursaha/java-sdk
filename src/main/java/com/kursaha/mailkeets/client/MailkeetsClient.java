package com.kursaha.mailkeets.client;

import com.kursaha.mailkeets.dto.MailRequestDto;

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
     * @return String identifier to trace the request
     */
    String send (
            String subject,
            String to,
            String from,
            String fromName,
            String body
    );

    /**
     * request to send mail
     * @param requestDto requires dto to send email
     * @return String identifier to trace the request
     */
    String sendMail(MailRequestDto requestDto);
}
