package com.kursaha.engagedatadrive.client;

import com.kursaha.engagedatadrive.dto.EventFlowRequestDto;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Api interface of Engage-data-drive
 */
public interface EngageDataDriveService {

    /**
     * Request to execute send-event api
     * @param apiKey user api key
     * @param requestDto holds the payload
     * @return void type
     */
    @POST("event-flows/signal")
    @Headers({"Content-type: application/json"})
    Call<Void> sendEvent(
            @Header("Authorization") String apiKey,
            @Body EventFlowRequestDto requestDto
    );
}
