package com.kursaha.engagedatadrive.dto;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NonNull;

@Data
public class EventFlowRequestDto {
    @NonNull
    @SerializedName("userId")
    private final String userId;
    @NonNull
    @SerializedName("stepNodeId")
    private final String stepNodeId;
    @NonNull
    @SerializedName("data")
    private final JsonObject data;
}
