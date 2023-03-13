package com.kursaha.engagedatadrive.client;

import com.google.gson.Gson;
import com.kursaha.engagedatadrive.dto.EventFlowRequestDto;
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
 * Api interface of Engage-data-drive
 */
public class EngageDataDriveService {

    private final HttpClient httpClient;

    public EngageDataDriveService() {
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
     * Request to execute send-event api
     * @param apiKey user api key
     * @param requestDto holds the payload
     * @param baseUrl baseurl of edd
     * @return void type
     */
    void sendEventByIdentifier(String apiKey, EventFlowRequestDto requestDto, String baseUrl) throws IOException {
        final String postUrl = baseUrl + "event-flows/signal";
        ClassicHttpRequest req = new BasicClassicHttpRequest(Method.POST, postUrl);
        try {
            if(!requestDto.getSignals().isEmpty()) {
                InputStreamEntity postingString = new InputStreamEntity(new ByteArrayInputStream(new Gson().toJson(requestDto).getBytes()), ContentType.APPLICATION_JSON);
                req.setHeaders(getRequestHeaders(apiKey));

                req.setEntity(postingString);

                HttpClientResponseHandler<String> responseHandler = new BasicHttpClientResponseHandler();
                final String responseBody = httpClient.execute(req, responseHandler);
                System.out.println("Store data into server: " + requestDto.getSignals().size());
            }

        } catch (IOException e) {
            //throw new IOException(e);
        }
    }
}
