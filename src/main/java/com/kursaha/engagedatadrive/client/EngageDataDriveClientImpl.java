package com.kursaha.engagedatadrive.client;

import com.google.gson.*;
import com.kursaha.Credentials;
import com.kursaha.common.ErrorMessageDto;
import com.kursaha.engagedatadrive.dto.*;
import com.kursaha.engagedatadrive.dto.EventFlowRequestDto.SignalPayload;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    private final ConcurrentLinkedQueue<Object> signalHolder;

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
                            List<Object> signals = new LinkedList<>();
                            int i = 0;
                            while (!signalHolder.isEmpty() && ++i < MAX_BATCH_SIZE) {
                                Object signal = signalHolder.poll();
                                if (signal != null) {
                                    signals.add(signal);
                                }
                            }
                            if (!signals.isEmpty()) {
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
            Map<String, Object> extraFields,
            JsonObject data,
            UUID identifier,
            Map<String, Map<String, Instant>> dynamicSleepNode /* sleepNodeId: { before:..., or after:... } */
    ) {
        emitterId = emitterId.trim();
        if(emitterId.split(" ").length > 1) {
            throw new RuntimeException("emitter id should not have any space");
        }
        for (Map.Entry<String, Object> extra : extraFields.entrySet()) {
            if(extra.getValue() instanceof String) {
                data.addProperty(extra.getKey(), (String) extra.getValue());
            } else if(extra.getValue() instanceof Number) {
                data.addProperty(extra.getKey(), (Number) extra.getValue());
            } else if(extra.getValue() instanceof Boolean) {
                data.addProperty(extra.getKey(), (Boolean) extra.getValue());
            } else {
                LOGGER.error("extra-field {} datatype not supported", extra);
            }
        }

        dynamicSleepNode.forEach((sleepNodeId, time) -> {
            JsonObject jsonObject = new JsonObject();
            time.forEach((key, value) -> {
                jsonObject.addProperty(key, value.toString());
            });
            data.add(sleepNodeId, jsonObject);
        });

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
        signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier, payload.getDynamicSleepNode());
    }

    @Override
    public void signal(UUID identifier, String stepNodeId, String emitterId, SignalMessagePayload payload) {
        JsonObject data = new JsonObject();
        if (payload.getPhoneNumber() == null || payload.getPhoneNumber().isBlank()) {
            throw new RuntimeException("phone number is missing");
        }
        data.addProperty("phone_number", payload.getPhoneNumber());
        signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier, payload.getDynamicSleepNode());
    }

    @Override
    public void signal(UUID identifier, String stepNodeId, String emitterId, SignalFcmNotificationPayload payload) {
        JsonObject data = payload.getAsJsonObject();
        signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier, payload.getDynamicSleepNode());
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
        signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier, payload.getDynamicSleepNode());
    }

    @Override
    public void signal(UUID identifier, String stepNodeId, String emitterId, SignalMailPayload payload) {
        JsonObject data = new JsonObject();
        if (payload.getEmail() == null || payload.getEmail().isBlank()) {
            throw new RuntimeException("email is missing");
        }
        data.addProperty("email", payload.getEmail());
        signalInternal(stepNodeId, emitterId, payload.getExtraFields(), data, identifier, payload.getDynamicSleepNode());
    }

    private void sendEventFlow(Object signals) {
        signalHolder.add(signals);
        synchronized (syncObj) {
            syncObj.notify();
        }
    }

    private void sendEventFlow(List<Object> signals) {
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

    @Override
    public void sendCustomerData(String customerId, CustomerData customerData) throws IOException {
        Call<Void> repos = sendCustomerDataInternal(customerId, customerData);
        Response<Void> response = repos.execute();
        if (!response.isSuccessful()) {
            try {
                ResponseBody responseBody = response.errorBody();
                if (responseBody == null) {
                    throw new IOException("Error Response body is null");
                }
                Reader reader = responseBody.charStream();
                ErrorMessageDto errorMessageDto = gson.fromJson(reader, ErrorMessageDto.class);
                LOGGER.error("failed to execute request : {}", errorMessageDto);
                throw new IOException("Failed to execute request. " + errorMessageDto);
            } catch (JsonIOException | JsonSyntaxException je) {
                throw new RuntimeException(je);
            }
        }
    }

    @Override
    public void sendCustomerData(String customerId, CustomerData customerData, com.kursaha.common.Callback callback) {
        Call<Void> repos = sendCustomerDataInternal(customerId, customerData);
        repos.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    callback.onFailure();
                } else {
                    callback.onSuccess();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    private Call<Void> sendCustomerDataInternal(String customerId, CustomerData customerData) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer id can't be null or blank");
        } else if (!customerData.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Given format of email is incorrect.");
        } else if (customerData.getDob() != null && isValidIso8601DateTime(customerData.getDob())) {
            throw new IllegalArgumentException("Not an valid ISO-8601 date format");
        }

        CustomerPartialUpdateDto dto = new CustomerPartialUpdateDto(customerId, customerData);

        return service.sendCustomerData("Bearer " + apiKey, dto);
    }

    private boolean isValidIso8601DateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        try {
            formatter.parse(dateTimeString);
            return true; // Parsing successful; it's a valid ISO 8601 date-time.
        } catch (DateTimeParseException e) {
            return false; // Parsing failed; it's not a valid ISO 8601 date-time.
        }
    }

    @Override
    public void trace(String customerId, String eventType, EventData eventData) {
        TraceRequestDto dto = new TraceRequestDto();
        sendEventFlow(dto);
    }
}
