package com.kursaha.engagedatadrive.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.Map;

@Data
public class SignalMailPayload {

    public SignalMailPayload(String email) {
        this(email, null);
    }

    public SignalMailPayload(String email, Map<String, String> extraFields) {
        this.email = email;
        this.extraFields = extraFields;
    }


    @NonNull
    private final String email;

    private final Map<String, String> extraFields;
}
