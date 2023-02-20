package com.kursaha.engagedatadrive.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Payload for the start event
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StartEventPayload extends EventPayload {
    /**
     * Phone number of the emitter
     */
    private String phoneNumber;
    /**
     * email of the emitter
     */
    private String email;
}
