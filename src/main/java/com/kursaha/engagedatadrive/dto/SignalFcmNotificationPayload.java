package com.kursaha.engagedatadrive.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Payload for fcm notification event
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SignalFcmNotificationPayload extends EventPayload {

    /**
     * fcm token of the emitter
     */
    @NonNull
    private String fcmToken;
}
