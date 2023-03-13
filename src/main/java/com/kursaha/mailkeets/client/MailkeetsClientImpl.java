package com.kursaha.mailkeets.client;

import com.google.gson.Gson;
import com.kursaha.Credentials;
import com.kursaha.mailkeets.dto.MailRequestDto;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

/**
 * Mailkeets client implementation
 */
public class MailkeetsClientImpl implements MailkeetsClient {
    private static final Logger LOGGER = Logger.getLogger(MailkeetsClientImpl.class.getName());

    private final String apiKey;
    private final MailkeetsService service;
    private final Gson gson;
    private final String baseUrl;

    /**
     * Constructor
     *
     * @param credentials credentials object
     * @param gson        ""
     * @param baseUrl     baseurl of api
     * @param executor    Executor used to process the API calls
     */
    public MailkeetsClientImpl(Credentials credentials, Gson gson, String baseUrl, Executor executor) {
        this.apiKey = credentials.getApiKey();
        this.gson = gson;
        this.baseUrl = baseUrl;
        this.service = new MailkeetsService();
    }

    @Override
    public String send(
            String subject,
            String to,
            String from,
            String fromName,
            String body
    ) {
        String requestIdentifier = UUID.randomUUID().toString();
        MailRequestDto requestDto = new MailRequestDto(fromName, from, to, subject, body, requestIdentifier);
        return sendMail(requestDto);
    }

    @Override
    public String sendMail(MailRequestDto requestDto) {
        try {
            service.sendMail(requestDto, "Bearer " + apiKey, baseUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return requestDto.getRequestIdentifier();
    }
}
