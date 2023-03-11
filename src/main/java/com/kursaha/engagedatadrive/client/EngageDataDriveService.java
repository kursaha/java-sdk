package com.kursaha.engagedatadrive.client;

import com.google.gson.Gson;
import com.kursaha.engagedatadrive.dto.EventFlowRequestDto;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
import org.apache.hc.core5.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.CyclicBarrier;

/**
 * Api interface of Engage-data-drive
 */
public class EngageDataDriveService {

//    /**
//     * Request to execute send-event api
//     * @param apiKey user api key
//     * @param requestDto holds the payload
//     * @return void type
//     */
//    @POST("event-flows/signal")
//    @Headers({"Content-type: application/json"})
//    Call<Void> sendEventByIdentifier(
//            @Header("Authorization") String apiKey,
//            @Body EventFlowRequestDto requestDto
//    );

//    private final Logger log = LoggerFactory.getLogger(EngageDataDriveService.this.getClass());

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

    void sendEventByIdentifier(String api, EventFlowRequestDto requestDto, String baseUrl) throws IOException {
        final String postUrl = baseUrl + "event-flows/signal";
        ClassicHttpRequest req = new BasicClassicHttpRequest(Method.POST, postUrl);
        try {
            if(!requestDto.getSignals().isEmpty()) {
                InputStreamEntity postingString = new InputStreamEntity(new ByteArrayInputStream(new Gson().toJson(requestDto).getBytes()), ContentType.APPLICATION_JSON);
                req.setHeaders(getRequestHeaders(api));

                req.setEntity(postingString);

                HttpClientResponseHandler<String> responseHandler = new BasicHttpClientResponseHandler();
                final String responseBody = httpClient.execute(req, responseHandler);
                System.out.println("Response for create order: " + responseBody);
            }
//            HttpEntity entity = httpResponse.getEntity();

//            if (httpResponse.getCode() != HttpStatus.SC_OK) {
            // TODO: 06/12/22 Handle network errors and add retries
//                log.error("Failed to create order: {}", EntityUtils.toString(entity));
//            }

        } catch (IOException e) {
            throw new IOException(e);
        }
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
    }
}
