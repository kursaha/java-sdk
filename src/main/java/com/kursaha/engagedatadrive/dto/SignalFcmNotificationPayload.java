package com.kursaha.engagedatadrive.dto;

import com.google.gson.JsonObject;
import com.kursaha.engagedatadrive.dto.enumeration.FcmTokenType;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Payload for fcm notification event
 */

@EqualsAndHashCode(callSuper = true)
public class SignalFcmNotificationPayload extends EventPayload {

    /**
     * fcm token of the emitter
     */
    @NonNull
    private final String token;
    /**
     * Fcm Token Type
     */
    @NonNull
    private final FcmTokenType fcmTokenType;

    /**
     * Json representative of the signal Fcm Notification Payload
     */
    private final JsonObject data;

    /**
     * Constructor
     * @param token String token
     * @param fcmTokenType Token Type
     */
    public SignalFcmNotificationPayload(@NonNull String token, @NonNull FcmTokenType fcmTokenType) {
        this.token = token;
        this.fcmTokenType = fcmTokenType;
        this.data = new JsonObject();
    }

    /**
     * add fields to the data and return
     * @return JsonObject
     */
    public JsonObject getAsJsonObject() {
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
                throw new RuntimeException("fcm token type " + fcmTokenType + " not supported");
            }
        }
        return data;
    }
}

