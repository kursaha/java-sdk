package com.kursaha.engagedatadrive.dto;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NonNull;

@Data
public class EventFlowRequestDto {
    @SerializedName("id")
    @NonNull
    private final String emitterId;
    @SerializedName("stepNodeId")
    @NonNull
    private final String stepNodeId;
    @SerializedName("data")
    @NonNull
    private final JsonObject data;
}
