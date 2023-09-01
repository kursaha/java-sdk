package com.kursaha.mailkeets.client;

import com.kursaha.common.MailkeetsException;
import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.dto.VerifiedDomainNameResponseDto;
import java.io.IOException;
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
     * @param contentType one of text/plain, text/html
     * @param body of email (mandatory field)
     * @param unsubscribedList Refer <a href="https://www.rfc-editor.org/rfc/rfc2369">Unsubscribe List</a> and
     *                         <a href="https://www.rfc-editor.org/rfc/rfc8058">Unsubscribed Post content</a>
     * @return String identifier to trace the request
     * @throws MailkeetsException If server failed to handle the request
     * @throws IOException If failed to communicate to the server
     * @throws RuntimeException If failed to parse request or response
     */
    String send (
            String subject,
            String to,
            String from,
            String fromName,
            String contentType,
            String body,
            String unsubscribedList
    ) throws MailkeetsException, IOException;

    /**
     * Synchronously send the request and return its response.
     * @param requestDto requires dto to send email
     * @throws IOException – if a problem occurred talking to the server.
     * @throws RuntimeException – (and subclasses) if an unexpected error occurs creating the request or decoding the response.
     * @throws MailkeetsException - if error happens in the server processing the request
     * @return identifier
     */
    String sendMail(MailRequestDto requestDto) throws MailkeetsException, IOException;

    /**
     * request to get all verified domain names
     * @return list of verified domain names
     * @throws Exception if there is any error
     */
    List<VerifiedDomainNameResponseDto> getVerifiedDomains() throws Exception;
}
