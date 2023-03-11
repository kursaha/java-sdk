package com.kursaha.engagedatadrive.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kursaha.Credentials;
import com.kursaha.config.QueueManagement;
import com.kursaha.engagedatadrive.dto.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * EngageDataDriveClientImpl is a class that can handle Engage-data-drive api
 */
public class EngageDataDriveClientImpl implements EngageDataDriveClient {

    private static final Logger LOGGER = Logger.getLogger(EngageDataDriveClientImpl.class.getName());
    private final String apiKey;
    private final String baseUrl;
    private final EngageDataDriveService service;
    private final Gson gson;
    private ConcurrentLinkedDeque<EventFlowRequestDto.SignalPayload> dtoqueue = new ConcurrentLinkedDeque<>();

    private QueueManagement<EventFlowRequestDto.SignalPayload> queueManagement;

    private static boolean firstStep = true;

    private ScheduledThreadPoolExecutor executor;

    /**
     * Constructor of EngageDataDriveClientImpl
     *
     * @param credentials customer credential
     * @param gson        object of Gson
     * @param baseUrl     baseurl of api
     * @param executor    Executor used to process the API calls
     */
    public EngageDataDriveClientImpl(Credentials credentials, Gson gson, String baseUrl, Executor executor) {
        this.apiKey = credentials.getApiKey();
        this.baseUrl = baseUrl;
        service = new EngageDataDriveService();
        this.gson = gson;

        this.executor = new ScheduledThreadPoolExecutor(1);
        this.queueManagement = new QueueManagement<>(5, dtoqueue);
    }

    private String signalInternal(String stepNodeId, String emitterId, Map<String, String> extraFields, JsonObject data, UUID identifier) {
        for (Map.Entry<String, String> extra : extraFields.entrySet()) {
            data.addProperty(extra.getKey(), extra.getValue());
        }
        String requestIdentifier = UUID.randomUUID().toString();
        EventFlowRequestDto.SignalPayload signalPayload = new EventFlowRequestDto.SignalPayload(emitterId, stepNodeId, data, requestIdentifier);

        checkQueue(signalPayload);

//        dtoqueue.add(signalPayload);

        if(firstStep) {
           schedule(identifier);
            executor.scheduleWithFixedDelay(queueManagement, 0, 1, TimeUnit.MICROSECONDS);
        }

        if(executor.isShutdown()) {
            schedule(identifier);
            executor.scheduleWithFixedDelay(queueManagement, 0, 1, TimeUnit.MICROSECONDS);
        }

        firstStep = false;
        return requestIdentifier;
    }

    @Override
    public String signal(UUID identifier, String stepNodeId, String emitterId) {
        String requestIdentifier = UUID.randomUUID().toString();
        EventFlowRequestDto.SignalPayload signalPayload = new EventFlowRequestDto.SignalPayload(emitterId, stepNodeId, null, requestIdentifier);
        sendEventFlow(List.of(signalPayload), identifier);
        return requestIdentifier;
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

    private void sendEventFlow(List<EventFlowRequestDto.SignalPayload> signals, UUID requestIdentifier) {
        EventFlowRequestDto requestDto = new EventFlowRequestDto(requestIdentifier.toString(), signals);
        try {
            service.sendEventByIdentifier("Bearer " + apiKey, requestDto, baseUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void schedule(UUID identifier) {
        executor.scheduleAtFixedRate(() -> {
            List<EventFlowRequestDto.SignalPayload> signalPayloads = new ArrayList<>(dtoqueue);
            sendEventFlow(signalPayloads, identifier);
            for (int i = 0; i < signalPayloads.size(); i++) {
                dtoqueue.poll();
            }
        }, 0, 1, TimeUnit.MILLISECONDS);
    }

    private void checkQueue(EventFlowRequestDto.SignalPayload signalPayload) {
        synchronized (dtoqueue) {
            if(dtoqueue.size() > 500) {
                try {
                    dtoqueue.wait(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            dtoqueue.add(signalPayload);
        }
    }

}
