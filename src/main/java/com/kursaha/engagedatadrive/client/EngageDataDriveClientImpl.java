package com.kursaha.engagedatadrive.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.kursaha.Credentials;
import com.kursaha.engagedatadrive.dto.EventFlowRequestDto;
import com.kursaha.common.ErrorMessageDto;
import com.kursaha.engagedatadrive.dto.SignalMailPayload;
import com.kursaha.engagedatadrive.exception.EddException;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Map;

/**
 * EngageDataDriveClientImpl is a class that can handle Engage-data-drive api
 */
public class EngageDataDriveClientImpl implements EngageDataDriveClient {

    // event Id
    private final String apiKey;
    private final EngageDataDriveService service;
    private final Gson gson;

    /**
     * Constructor of EngageDataDriveClientImpl
     * @param credentials customer credential
     * @param gson ""
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


    @Override
    public void signal(Long eventflowId, String stepNodeId, String emitterId) throws EddException {
        EventFlowRequestDto eventFlowRequestDto = new EventFlowRequestDto(eventflowId, stepNodeId, null, emitterId);
        sendEventFlow(eventFlowRequestDto);
    }


    @Override
    public void signalMail(Long eventflowId, String stepNodeId, String emitterId, SignalMailPayload signalMailPayloadData) throws EddException {

        JsonObject data = new JsonObject();
        if (signalMailPayloadData.getEmail() == null || signalMailPayloadData.getEmail().isBlank()) {
            throw new EddException("email is missing");
        }
        data.addProperty("email", signalMailPayloadData.getEmail());

        if(signalMailPayloadData.getExtraFields() != null) {
            for (Map.Entry<String, String> extra : signalMailPayloadData.getExtraFields().entrySet()) {
                data.addProperty(extra.getKey(), extra.getValue());
            }
        }

        EventFlowRequestDto eventFlowRequestDto = new EventFlowRequestDto(eventflowId, stepNodeId, data, emitterId);
        sendEventFlow(eventFlowRequestDto);
    }

    private void sendEventFlow(EventFlowRequestDto eventFlowRequestDto) throws EddException {
        try {
            Call<Void> repos = service.sendEvent("Bearer " + apiKey, eventFlowRequestDto);
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
}
