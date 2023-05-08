package com.kursaha.engagedatadrive.client;

import com.google.gson.*;
import com.kursaha.Credentials;
import com.kursaha.common.ErrorMessageDto;
import com.kursaha.engagedatadrive.dto.*;
import com.kursaha.engagedatadrive.dto.EventFlowRequestDto.SignalPayload;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * EngageDataDriveClientImpl is a class that can handle Engage-data-drive api
 */
public class EngageDataDriveClientImpl implements EngageDataDriveClient {
    private static final int MAX_BATCH_SIZE = 500;

    private static final Logger LOGGER =  LoggerFactory.getLogger(EngageDataDriveClientImpl.class);
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
                                LOGGER.info("got interrupt in wait");
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
        emitterId = emitterId.trim();
        if(emitterId.split(" ").length > 1) {
            throw new RuntimeException("emitter id should not have any space");
        }
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
    public void signal(UUID identifier, String stepNodeId, String emitterId, SignalFcmNotificationPayload payload) {
        JsonObject data = new JsonObject();
        if (payload.getFcmToken() == null || payload.getFcmToken().isBlank()) {
            throw new RuntimeException("fcm token is missing");
        }
        data.addProperty("fcm_token", payload.getFcmToken());
        signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier);
    }

    @Override
    public void signal(UUID identifier, String stepNodeId, String emitterId, SignalInteraktWhatsappPayload payload) {
        JsonObject data = new JsonObject();
        if (payload.getPhoneNumber() == null || payload.getPhoneNumber().isBlank()) {
            throw new RuntimeException("Phone Number is missing");
        }
        if (payload.getCountryCode() == null || payload.getCountryCode().isBlank()) {
            throw new RuntimeException("Country code is missing");
        }
        data.addProperty("countryCode", payload.getCountryCode());
        data.addProperty("phoneNumber", payload.getPhoneNumber());

        if (payload.getBodyValues() != null) {
            data.add("bodyValues", new JsonParser().parse(gson.toJson(payload.getBodyValues())).getAsJsonArray());
        }
        if (payload.getHeaderValues() != null) {
            data.add("headerValues", new JsonParser().parse(gson.toJson(payload.getHeaderValues())).getAsJsonArray());
        }
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
        LOGGER.info("Sending signal for {}", signals.size());
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
                        LOGGER.error("Failed to execute request {}", errorResponse);
                    } catch (JsonSyntaxException jse) {
                        if (responseAsString != null) {
                            LOGGER.error("Failed to execute request {}", responseAsString);
                        }
                    } catch (Exception ex) {
                        LOGGER.error("Failed to execute request {}", ex.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                LOGGER.error("Failed to execute request {}", t.getMessage());
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

    @Override
    public boolean isConnectedAndAuthenticated() throws IOException {
        return Objects.equals(Objects.requireNonNull(service.ping("Bearer " + apiKey).execute().body()).getResponse(), "pong");
    }
}
