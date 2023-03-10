package com.kursaha.engagedatadrive.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Payload for mail signal event
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignalMailPayload extends EventPayload {

    /**
     * email id of the emitter
     */
    @NonNull
    private final String email;
}
