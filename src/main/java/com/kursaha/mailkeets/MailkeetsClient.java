package com.kursaha.mailkeets;

import com.fasterxml.jackson.databind.JsonNode;
import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.exception.MailkeetsSendMailException;
import com.kursaha.mailkeets.utils.MailkeetsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailkeetsClient {
    private final String apiKey;
    private final MailkeetsUtils mailkeetsUtils = new MailkeetsUtils();
    private final Logger log = LoggerFactory.getLogger(MailkeetsClient.this.getClass());

    public MailkeetsClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public JsonNode send (String subject, String to, String from, String fromName, String body) throws MailkeetsSendMailException {
        MailRequestDto requestDto = new MailRequestDto();
        requestDto.setTo(to);
        requestDto.setBody(body);
        requestDto.setSubject(subject);
        requestDto.setFromAddress(from);
        requestDto.setFromName(fromName);

        return mailkeetsUtils.sendMail(requestDto, apiKey);
    }
}
