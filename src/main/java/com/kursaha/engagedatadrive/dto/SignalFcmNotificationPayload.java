package com.kursaha.engagedatadrive.dto;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Payload for fcm notification event
 */

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SignalFcmNotificationPayload extends EventPayload {

    /**
     * fcm token of the emitter
     */
    @NonNull
    private String token;

    @NonNull
    private FcmTokenType fcmTokenType;

    public JsonObject convertToken() {
        JsonObject data = new JsonObject();
        switch (fcmTokenType) {
            case WEB_TOKEN : {
                data.addProperty("web_token", token);
                break;
            }
            case ANDROID_TOKEN : {
                data.addProperty("android_token", token);
                break;
            }
            case APNS_TOKEN : {
                data.addProperty("apns_token", token);
                break;
            }
            default : {
                throw new RuntimeException("fcm token type not supported");
            }
        }
        return data;
    }
}

