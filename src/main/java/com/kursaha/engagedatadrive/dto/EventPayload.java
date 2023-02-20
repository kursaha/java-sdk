package com.kursaha.engagedatadrive.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for event payload
 */
@Data
public abstract class EventPayload {

    /**
     * extra fields to pass to event
     */
    private final Map<String, String> extraFields;

    EventPayload() {
        this.extraFields = new HashMap<>();
    }

    /**
     * Method to add extra properties
     * @param key property name
     * @param value property value
     */
    public void addProperty(String key, String value) {
        extraFields.put(key, value);
    }
}
