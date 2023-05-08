package com.kursaha.engagedatadrive.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.util.List;

/**
 * Payload for Interakt whatsapp event
 */
@Data
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
public class SignalInteraktWhatsappPayload extends EventPayload {

    /**
     * Country code of the phone number
     */
    @NonNull
    private String countryCode;

    /**
     * Phone number of the customer
     */
    @NonNull
    private String phoneNumber;

    /**
     * headerValues of Interakt
     */
    private List<String> headerValues;

    /**
     * bodyValues of Interakt
     */
    private List<String> bodyValues;

}
