package com.kursaha.engagedatadrive.dto;

import lombok.AllArgsConstructor;

/**
 * This class represents the event dto
 */
@AllArgsConstructor
public class TraceRequestDto {
    private String customerId;

    private String eventType;

    private EventData data;
}
