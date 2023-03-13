package com.kursaha.engagedatadrive.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.kursaha.Credentials;
import com.kursaha.engagedatadrive.dto.*;
import com.kursaha.common.ErrorMessageDto;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * EngageDataDriveClientImpl is a class that can handle Engage-data-drive api
 */
public class EngageDataDriveClientImpl implements EngageDataDriveClient {

    private static final Logger LOGGER = Logger.getLogger(EngageDataDriveClientImpl.class.getName());
    private final String apiKey;
    private final EngageDataDriveService service;
    private final Gson gson;

    /**
     * Constructor of EngageDataDriveClientImpl
     *
     * @param credentials  customer credential
     * @param gson         object of Gson
     * @param baseUrl      baseurl of api
     * @param okHttpClient ok http client
     */
    public EngageDataDriveClientImpl(
            Credentials credentials,
            Gson gson,
            String baseUrl,
            OkHttpClient okHttpClient
    ) {
        this.apiKey = credentials.getApiKey();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(EngageDataDriveService.class);
        this.gson = gson;
    }

    private String signalInternal(
            String stepNodeId,
            String emitterId,
            Map<String, String> extraFields,
            JsonObject data,
            UUID identifier
    ) {
        for (Map.Entry<String, String> extra : extraFields.entrySet()) {
            data.addProperty(extra.getKey(), extra.getValue());
        }
        EventFlowRequestDto.SignalPayload signalPayload =
                new EventFlowRequestDto.SignalPayload(emitterId, stepNodeId, data, identifier.toString());

        return sendEventFlow(List.of(signalPayload));
    }

    @Override
    public String signal(UUID eventflowIdentifier, String stepNodeId, String emitterId) {
        EventFlowRequestDto.SignalPayload signalPayload =
                new EventFlowRequestDto.SignalPayload(emitterId, stepNodeId, null, eventflowIdentifier.toString());

        return sendEventFlow(List.of(signalPayload));
    }

    @Override
    public String signal(UUID identifier, String stepNodeId, String emitterId, StartEventPayload payload) {
        JsonObject data = new JsonObject();
        if (payload.getEmail() == null || payload.getEmail().isBlank()) {
            data.addProperty("email", payload.getEmail());
        }

        if (payload.getPhoneNumber() == null || payload.getPhoneNumber().isBlank()) {
            data.addProperty("phone_number", payload.getPhoneNumber());
        }
        return signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier);
    }

    @Override
    public String signal(UUID identifier, String stepNodeId, String emitterId, SignalMessagePayload payload) {
        JsonObject data = new JsonObject();
        if (payload.getPhoneNumber() == null || payload.getPhoneNumber().isBlank()) {
            throw new RuntimeException("phone number is missing");
        }
        data.addProperty("phone_number", payload.getPhoneNumber());
        return signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier);
    }

    @Override
    public String signal(UUID identifier, String stepNodeId, String emitterId, SignalMailPayload payload) {
        JsonObject data = new JsonObject();
        if (payload.getEmail() == null || payload.getEmail().isBlank()) {
            throw new RuntimeException("email is missing");
        }
        data.addProperty("email", payload.getEmail());
        return signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier);
    }

    private String sendEventFlow(List<EventFlowRequestDto.SignalPayload> signals) {
        // to trace the request
        UUID requestIdentifier = UUID.randomUUID();

        EventFlowRequestDto requestDto = new EventFlowRequestDto(requestIdentifier.toString(), signals);
        Call<Void> repos = selectApi(requestDto);
        repos.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    String responseAsString = null;
                    try {
                        StringBuilder sb = new StringBuilder();
                        for (int ch; (ch = response.errorBody().charStream().read()) != -1; ) {
                            sb.append((char) ch);
                        }
                        responseAsString = sb.toString();
                        ErrorMessageDto errorResponse = gson.fromJson(responseAsString, ErrorMessageDto.class);
                        LOGGER.log(Level.SEVERE, "Failed to execute request", errorResponse);
                    } catch (JsonSyntaxException jse) {
                        if (responseAsString != null) {
                            LOGGER.log(Level.SEVERE, "Failed to execute request", responseAsString);
                        }
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, "Failed to execute request", ex);
                    }
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                LOGGER.log(Level.SEVERE, "Failed to execute request", t);
            }
        });

        return requestIdentifier.toString();
    }

    private Call<Void> selectApi(EventFlowRequestDto eventFlowRequestDto) {
        return service.sendEventByIdentifier("Bearer " + apiKey, eventFlowRequestDto);
    }
}
