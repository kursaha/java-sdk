package com.kursaha.engagedatadrive.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.kursaha.Credentials;
import com.kursaha.common.ErrorMessageDto;
import com.kursaha.engagedatadrive.dto.EventFlowRequestDto;
import com.kursaha.engagedatadrive.dto.EventFlowRequestDto.SignalPayload;
import com.kursaha.engagedatadrive.dto.SignalMailPayload;
import com.kursaha.engagedatadrive.dto.SignalMessagePayload;
import com.kursaha.engagedatadrive.dto.StartEventPayload;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * EngageDataDriveClientImpl is a class that can handle Engage-data-drive api
 */
public class EngageDataDriveClientImpl implements EngageDataDriveClient {
    private static final int MAX_BATCH_SIZE = 500;

    private static final Logger LOGGER = Logger.getLogger(EngageDataDriveClientImpl.class.getName());
    private final String apiKey;
    private final EngageDataDriveService service;
    private final Gson gson;

    private final Object syncObj = new Object();
    private final ConcurrentLinkedQueue<SignalPayload> signalHolder;

    private volatile boolean processingMessages;

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
        this.signalHolder = new ConcurrentLinkedQueue<>();
        // ignore me
        Thread consumerThread = new Thread(
                () -> {
                    while (true) {
                        if (!signalHolder.isEmpty()) {
                            processingMessages = true;
                            List<SignalPayload> signals = new LinkedList<>();
                            int i = 0;
                            while (!signalHolder.isEmpty() && ++i < MAX_BATCH_SIZE) {
                                SignalPayload signal = signalHolder.poll();
                                if (signal != null) {
                                    signals.add(signal);
                                }
                            }
                            if (signals.size() > 0) {
                                sendEventFlow(signals);
                            }
                        } else {
                            processingMessages = false;
                            try {
                                synchronized (syncObj) {
                                    syncObj.wait(3_000L);
                                }
                            } catch (InterruptedException e) {
                                LOGGER.fine("got interrupt in wait");
                                // ignore me
                            }
                        }
                    }
                });
        consumerThread.start();
    }

    private void signalInternal(
            String stepNodeId,
            String emitterId,
            Map<String, String> extraFields,
            JsonObject data,
            UUID identifier
    ) {
        for (Map.Entry<String, String> extra : extraFields.entrySet()) {
            data.addProperty(extra.getKey(), extra.getValue());
        }
        SignalPayload signalPayload =
                new SignalPayload(emitterId, stepNodeId, data, identifier.toString());

        sendEventFlow(signalPayload);
    }

    @Override
    public void signal(UUID eventflowIdentifier, String stepNodeId, String emitterId) {
        SignalPayload signalPayload =
                new SignalPayload(emitterId, stepNodeId, null, eventflowIdentifier.toString());

        sendEventFlow(signalPayload);
    }

    @Override
    public void signal(UUID identifier, String stepNodeId, String emitterId, StartEventPayload payload) {
        JsonObject data = new JsonObject();
        if (payload.getEmail() == null || payload.getEmail().isBlank()) {
            data.addProperty("email", payload.getEmail());
        }

        if (payload.getPhoneNumber() == null || payload.getPhoneNumber().isBlank()) {
            data.addProperty("phone_number", payload.getPhoneNumber());
        }
        signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier);
    }

    @Override
    public void signal(UUID identifier, String stepNodeId, String emitterId, SignalMessagePayload payload) {
        JsonObject data = new JsonObject();
        if (payload.getPhoneNumber() == null || payload.getPhoneNumber().isBlank()) {
            throw new RuntimeException("phone number is missing");
        }
        data.addProperty("phone_number", payload.getPhoneNumber());
        signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier);
    }

    @Override
    public void signal(UUID identifier, String stepNodeId, String emitterId, SignalMailPayload payload) {
        JsonObject data = new JsonObject();
        if (payload.getEmail() == null || payload.getEmail().isBlank()) {
            throw new RuntimeException("email is missing");
        }
        data.addProperty("email", payload.getEmail());
        signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier);
    }

    private void sendEventFlow(SignalPayload signals) {
        signalHolder.add(signals);
        synchronized (syncObj) {
            syncObj.notify();
        }
    }

    private void sendEventFlow(List<SignalPayload> signals) {
        LOGGER.fine("Sending signal for " + signals.size());
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
    }

    private Call<Void> selectApi(EventFlowRequestDto eventFlowRequestDto) {
        return service.sendEventByIdentifier("Bearer " + apiKey, eventFlowRequestDto);
    }

    @Override
    public boolean hasSignals() {
        return !signalHolder.isEmpty() || processingMessages;
    }
}
