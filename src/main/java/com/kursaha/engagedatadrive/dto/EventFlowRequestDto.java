package com.kursaha.engagedatadrive.dto;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NonNull;

/**
 * EventFlowRequestDto holds the payload which is required for the EngageDataDriveService api
 */
@Data
public class EventFlowRequestDto {
//    @NonNull
    private final Long eventflowId;
    @NonNull
    private final String stepNodeId;
    private final JsonObject data;
    @NonNull
    private final String emitterId;

}
