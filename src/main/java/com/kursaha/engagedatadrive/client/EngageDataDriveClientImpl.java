package com.kursaha.engagedatadrive.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kursaha.Credentials;
import com.kursaha.engagedatadrive.dto.EventFlowRequestDto;
import com.kursaha.ErrorMessageDto;
import com.kursaha.engagedatadrive.exception.EddException;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class EngageDataDriveClientImpl implements EngageDataDriveClient {

    private static final String BASE_URL = "https://kursaha.com/api/";
    // event Id
    private final String id;
    // api key id
    private final String apiKey;
    private final EngageDataDriveService service;
    private final Gson gson;

    public EngageDataDriveClientImpl(Credentials credentials, String id) {
        this.id = id;
        this.apiKey = credentials.getApiKey();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(EngageDataDriveService.class);
        gson = new Gson();
    }


    @Override
    public Void sendEventFlow(String emitterId, String stepNodeId, JsonObject data) throws EddException {
        EventFlowRequestDto eventFlowRequestDto = new EventFlowRequestDto(emitterId, stepNodeId, data);
        return sendEventFlow(eventFlowRequestDto);
    }

    @Override
    public Void sendEventFlow(EventFlowRequestDto eventFlowRequestDto) throws EddException {
        try {
            Call<Void> repos = service.sendEvent(eventFlowRequestDto, id, "Bearer " + apiKey);
            Response<Void> response = repos.execute();
            if (!response.isSuccessful()) {
                try {
                    ErrorMessageDto errorResponse = gson.fromJson(response.errorBody().charStream(), ErrorMessageDto.class);
                    throw new EddException(errorResponse);
                } catch (Exception ex) {
                    throw new EddException(ex);
                }
            }
            return response.body();
        } catch (IOException | EddException e) {
            throw new EddException(e);
        }
    }
}
