package com.kursaha.mailkeets.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.exception.MailkeetsSendMailException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MailkeetsUtils {

    private final Logger log = LoggerFactory.getLogger(MailkeetsUtils.class);
    private final String baseUrl = "https://mailkeets.kursaha.com/api";
    private Gson gson = new Gson();
    private final HttpClient httpClient;

    public MailkeetsUtils() {
        this.httpClient = HttpClientBuilder.create().build();
    }

    private Header[] getRequestHeaders(String apiKey) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-type", "application/json");
        headers[1] = new BasicHeader("Authorization", "Bearer " + apiKey);
        return headers;
    }

    public JsonNode sendMail (MailRequestDto requestDto, String apiKey) throws MailkeetsSendMailException {
        final String postUrl = baseUrl + "/mail-send";
        HttpPost postRequest = new HttpPost(postUrl);
        try {
            StringEntity postingString = new StringEntity(gson.toJson(requestDto));
            postRequest.setHeaders(getRequestHeaders(apiKey));
            postRequest.setEntity(postingString);

            HttpResponse httpResponse = httpClient.execute(postRequest);
            log.info("Response for sending mail: {}", httpResponse);
            HttpEntity entity = httpResponse.getEntity();

            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.error("Failed to send mail: {}", EntityUtils.toString(entity));
                throw new MailkeetsSendMailException();
            }

            JsonNode response = null;
            if (entity != null) {
                response = new ObjectMapper().readTree(EntityUtils.toString(entity));
            }
            return response;
        } catch (IOException | MailkeetsSendMailException e) {
            throw new MailkeetsSendMailException(e);
        }
    }

}
