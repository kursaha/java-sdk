package com.kursaha.engagedatadrive.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Payload for message signal event
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignalMessagePayload extends EventPayload {

    /**
     * phone number of the emitter
     */
    @NonNull
    private String phoneNumber;


}
