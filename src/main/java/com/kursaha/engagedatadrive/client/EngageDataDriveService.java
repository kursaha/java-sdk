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
     * @param requestDto holds the payload
     * @param id id of event-flow
     * @param apiKey user api key
     * @return void type
     */
    @POST("event-flows/signal/{id}")
    @Headers({"Content-type: application/json"})
    Call<Void> sendEvent(
            @Body EventFlowRequestDto requestDto,
            @Path("id") Long id,
            @Header("Authorization") String apiKey
    );
}
