package com.kursaha.engagedatadrive.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kursaha.Credentials;
import com.kursaha.engagedatadrive.dto.EventFlowRequestDto;
import com.kursaha.common.ErrorMessageDto;
import com.kursaha.engagedatadrive.exception.EddException;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class EngageDataDriveClientImpl implements EngageDataDriveClient {

    private static final String BASE_URL = "https://edd.kursaha.com/api/";
    // event Id
    private final String apiKey;
    private final EngageDataDriveService service;
    private final Gson gson;

    public EngageDataDriveClientImpl(Credentials credentials, Gson gson) {
        this.apiKey = credentials.getApiKey();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(EngageDataDriveService.class);
        this.gson = gson;
    }


    @Override
    public void sendEventFlow(Long eventflowId, String userId, String stepNodeId, JsonObject data) throws EddException {
        EventFlowRequestDto eventFlowRequestDto = new EventFlowRequestDto(userId, stepNodeId, data);
        sendEventFlow(eventflowId, eventFlowRequestDto);
    }

    @Override
    public void sendEventFlow(Long eventflowId, EventFlowRequestDto eventFlowRequestDto) throws EddException {
        try {
            Call<Void> repos = service.sendEvent(eventFlowRequestDto, eventflowId, "Bearer " + apiKey);
            Response<Void> response = repos.execute();
            if (!response.isSuccessful()) {
                try {
                    ErrorMessageDto errorResponse = gson.fromJson(response.errorBody().charStream(), ErrorMessageDto.class);
                    throw new EddException(errorResponse);
                } catch (Exception ex) {
                    throw new EddException(ex);
                }
            }
        } catch (IOException | EddException e) {
            throw new EddException(e);
        }
    }
}
