package com.kursaha.engagedatadrive.dto;

import com.google.gson.JsonObject;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * EventFlowRequestDto holds the payload which is required for the EngageDataDriveService api
 */
@Data
public class EventFlowRequestDto {
    @NonNull
    private final String requestIdentifier;

    private final List<Object> signals;

    /**
     * Signal message
     */
    @Data
    public static final class SignalPayload {
        @NonNull
        private final String emitterId;
        @NonNull
        private final String stepNodeId;
        private final JsonObject data;
        @NonNull
        private final String eventflowIdentifier;
    }
}
