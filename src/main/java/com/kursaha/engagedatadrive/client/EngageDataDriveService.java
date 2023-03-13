package com.kursaha.engagedatadrive.client;

import com.kursaha.engagedatadrive.dto.EventFlowRequestDto;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.UUID;

/**
 * Api interface of Engage-data-drive
 */
public interface EngageDataDriveService {

    /**
     * Request to execute send-event api
     * @param apiKey user api key
     * @param graphIdentifier identifier for the event flow graph
     * @param requestDto holds the payload
     * @return void type
     */
    @POST("event-flows/signal")
    @Headers({"Content-type: application/json"})
    Call<Void> sendEventByIdentifier(
            @Header("Authorization") String apiKey,
            @Body EventFlowRequestDto requestDto
    );
}
