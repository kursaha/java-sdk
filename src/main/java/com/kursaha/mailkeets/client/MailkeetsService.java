package com.kursaha.mailkeets.client;

import com.google.gson.Gson;
import com.kursaha.mailkeets.dto.MailRequestDto;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
import org.apache.hc.core5.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Mailkeets service
 */
public class MailkeetsService {

    private final HttpClient httpClient;

    public MailkeetsService() {
        this.httpClient = HttpClientBuilder.create().build();
    }

    Header[] getRequestHeaders(String apiKey) {
        Header[] headers = new Header[3];
        headers[0] = new BasicHeader("Accept", "application/json");
        headers[1] = new BasicHeader("Content-type", "application/json");
        headers[2] = new BasicHeader("Authorization", apiKey);
        return headers;
    }

    /**
     * Send mail
     * @param requestDto request dto
     * @param apiKey api key
     * @return call to response
     */
    void sendMail(MailRequestDto requestDto, String apiKey, String baseUrl) throws IOException {
        final String postUrl = baseUrl + "event-flows/signal";
        ClassicHttpRequest req = new BasicClassicHttpRequest(Method.POST, postUrl);
        try {
            if(requestDto != null) {
                InputStreamEntity postingString = new InputStreamEntity(new ByteArrayInputStream(new Gson().toJson(requestDto).getBytes()), ContentType.APPLICATION_JSON);
                req.setHeaders(getRequestHeaders(apiKey));

                req.setEntity(postingString);

                HttpClientResponseHandler<String> responseHandler = new BasicHttpClientResponseHandler();
                final String responseBody = httpClient.execute(req, responseHandler);
                System.out.println("Response for create order: " + responseBody);
            }

        } catch (IOException e) {
            //throw new IOException(e);
        }
    }


}
