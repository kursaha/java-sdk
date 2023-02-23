package com.kursaha.engagedatadrive.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.kursaha.Credentials;
import com.kursaha.engagedatadrive.dto.*;
import com.kursaha.common.ErrorMessageDto;
import com.kursaha.engagedatadrive.exception.EddException;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * EngageDataDriveClientImpl is a class that can handle Engage-data-drive api
 */
public class EngageDataDriveClientImpl implements EngageDataDriveClient {

    private final String apiKey;
    private final EngageDataDriveService service;
    private final Gson gson;

    /**
     * Constructor of EngageDataDriveClientImpl
     * @param credentials customer credential
     * @param gson object of Gson
     * @param baseUrl baseurl of api
     */
    public EngageDataDriveClientImpl(Credentials credentials, Gson gson, String baseUrl) {
        this.apiKey = credentials.getApiKey();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(EngageDataDriveService.class);
        this.gson = gson;
    }

    private void signalInternal(String stepNodeId, String emitterId, Map<String, String> extraFields, JsonObject data, UUID identifier) throws EddException {
        for (Map.Entry<String, String> extra : extraFields.entrySet()) {
            data.addProperty(extra.getKey(), extra.getValue());
        }

        EventFlowRequestDto eventFlowRequestDto = new EventFlowRequestDto(stepNodeId, data, emitterId);
        sendEventFlow(eventFlowRequestDto, identifier);
    }

    @Override
    public void signal(UUID identifier, String stepNodeId, String emitterId) throws EddException {
        EventFlowRequestDto eventFlowRequestDto = new EventFlowRequestDto(stepNodeId, null, emitterId);
        sendEventFlow(eventFlowRequestDto, identifier);
    }

    @Override
    public void signal(UUID identifier, String stepNodeId, String emitterId, StartEventPayload payload) throws EddException {
        JsonObject data = new JsonObject();
        if (payload.getEmail() == null || payload.getEmail().isBlank()) {
            data.addProperty("email", payload.getEmail());
        }

        if (payload.getPhoneNumber() == null || payload.getPhoneNumber().isBlank()) {
            throw new EddException("phone number is missing");
        }
        data.addProperty("phone_number", payload.getPhoneNumber());
        signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier);
    }

    @Override
    public void signal(UUID identifier, String stepNodeId, String emitterId, SignalMessagePayload payload) throws EddException {
        JsonObject data = new JsonObject();
        if (payload.getPhoneNumber() == null || payload.getPhoneNumber().isBlank()) {
            throw new EddException("phone number is missing");
        }
        data.addProperty("phone_number", payload.getPhoneNumber());
        signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier);
    }

    @Override
    public void signal(UUID identifier, String stepNodeId, String emitterId, SignalMailPayload payload) throws EddException {
        JsonObject data = new JsonObject();
        if (payload.getEmail() == null || payload.getEmail().isBlank()) {
            throw new EddException("phone number is missing");
        }
        data.addProperty("email", payload.getEmail());
        signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier);
    }

    private void sendEventFlow(EventFlowRequestDto eventFlowRequestDto, UUID identifier) throws EddException {
        try {
            Call<Void> repos = selectApi(identifier, eventFlowRequestDto);
            Response<Void> response = repos.execute();
            if (!response.isSuccessful()) {
                String responseAsString = null;
                try {
                    StringBuilder sb = new StringBuilder();
                    for (int ch; (ch = response.errorBody().charStream().read()) != -1; ) {
                        sb.append((char) ch);
                    }
                    responseAsString = sb.toString();
                    ErrorMessageDto errorResponse = gson.fromJson(responseAsString, ErrorMessageDto.class);
                    throw new EddException(errorResponse);
                } catch (JsonSyntaxException jse) {
                    if (responseAsString != null) {
                        throw new EddException(responseAsString);
                    }
                } catch (Exception ex) {
                    throw new EddException(ex);
                }
            }
        } catch (IOException | EddException e) {
            throw new EddException(e);
        }
    }

    private Call<Void> selectApi (UUID identifier, EventFlowRequestDto eventFlowRequestDto) throws EddException {
        return service.sendEventByIdentifier("Bearer " + apiKey, identifier, eventFlowRequestDto);
    }
}
