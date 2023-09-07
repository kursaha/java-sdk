package com.kursaha.engagedatadrive.client;

import com.kursaha.engagedatadrive.dto.CustomerPartialUpdateDto;
import com.kursaha.engagedatadrive.dto.EventFlowRequestDto;
import com.kursaha.engagedatadrive.dto.SdkPingResponse;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.UUID;

/**
 * Api interface of Engage-data-drive
 */
interface EngageDataDriveService {

    /**
     * Request to execute send-event api
     * @param apiKey user api key
     * @param requestDto holds the payload
     * @return void type
     */
    @POST("event-flows/signal")
    @Headers({"Content-type: application/json"})
    Call<Void> sendEventByIdentifier(
        @Header("Authorization") String apiKey,
        @Body EventFlowRequestDto requestDto
    );

    /**
     * Make call to server and ensure that the api key is valid
     * @param apiKey api key
     * @return true, if api key is valid
     */
    @GET("sdk/ping")
    Call<SdkPingResponse> ping(
        @Header("Authorization") String apiKey
    );

    /**
     * Request to send customer details
     * @param apiKey user apikey
     * @param customerPartialUpdateDto details of customer
     * @return void
     */
    @PATCH("customers")
    Call<Void> sendCustomerData(
        @Header("Authorization") String apiKey,
        @Body CustomerPartialUpdateDto customerPartialUpdateDto
    );

}
