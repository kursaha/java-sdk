package com.kursaha.mailkeets;

import com.google.gson.JsonObject;
import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.exception.MailkeetsSendMailException;
import com.kursaha.mailkeets.utils.MailkeetsUtilsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailkeetsClient {
    private final String apiKey;
    private final MailkeetsUtilsImpl mailkeetsUtils;

    public MailkeetsClient(String apiKey) {
        this.apiKey = apiKey;
        mailkeetsUtils = new MailkeetsUtilsImpl();
    }

    public JsonObject send (String subject, String to, String from, String fromName, String body) throws MailkeetsSendMailException {
        MailRequestDto requestDto = new MailRequestDto();
        requestDto.setTo(to);
        requestDto.setBody(body);
        requestDto.setSubject(subject);
        requestDto.setFromAddress(from);
        requestDto.setFromName(fromName);

        return mailkeetsUtils.sendMail(requestDto, apiKey);
    }
}
