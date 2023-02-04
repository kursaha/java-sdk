package com.kursaha.engagedatadrive.client;

import com.kursaha.engagedatadrive.dto.EventFlowRequestDto;
import retrofit2.Call;
import retrofit2.http.*;

public interface EngageDataDriveService {

    @POST("event-flows/signal/{id}")
    @Headers({"Content-type: application/json"})
    Call<Void> sendEvent(
            @Body EventFlowRequestDto requestDto,
            @Path("id") String id,
            @Header("Authorization") String apiKey
    );
}
