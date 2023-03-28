package com.kursaha.mailkeets.client;

import com.kursaha.mailkeets.dto.VerifiedDomainNameResponseDto;
import com.kursaha.mailkeets.dto.MailRequestDto;
import java.util.List;
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
     * @param unsubscribedList Refer <a href="https://www.rfc-editor.org/rfc/rfc2369">Unsubscribe List</a> and
     *                         <a href="https://www.rfc-editor.org/rfc/rfc8058">Unsubscribed Post content</a>
     * @return String identifier to trace the request
     */
    String send (
            String subject,
            String to,
            String from,
            String fromName,
            String body,
            String unsubscribedList
    );

    /**
     * request to send mail
     * @param requestDto requires dto to send email
     * @return String identifier to trace the request
     */
    String sendMail(MailRequestDto requestDto);

    /**
     * request to get all verified domain names
     * @return list of verified domain names
     */
    List<VerifiedDomainNameResponseDto> getVerifiedDomains() throws Exception;
}
