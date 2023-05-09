package com.kursaha.engagedatadrive.dto;

import lombok.Data;

import java.time.Instant;
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

    /**
     * dynamicSleepNode property
     */
    private final Map<String, Map<String, Instant>> dynamicSleepNode;

    EventPayload() {
        this.extraFields = new HashMap<>();
        this.dynamicSleepNode = new HashMap<>();
    }

    /**
     * Method to add extra properties
     * @param key property name
     * @param value property value
     */
    public void addProperty(String key, String value) {
        extraFields.put(key, value);
    }

    /**
     * Method to add Dynamic Sleep Node before time
     * If you want to execute a sleep node before a scheduled time
     * @param sleepNodeId id of sleep node
     * @param beforeTime takes an Instant time (Scheduled time)
     */
    public void addBeforeEventDateTime(String sleepNodeId, Instant beforeTime) {
        Map<String, Instant> before = new HashMap<>();
        before.put("beforeEventDateTime", beforeTime);
        dynamicSleepNode.put(sleepNodeId, before);
    }

    /**
     * Method to add Dynamic Sleep Node after time
     * If you want to execute a sleep node after a scheduled time
     * @param sleepNodeId id of sleep node
     * @param afterTime takes an Instant time (Scheduled time)
     */
    public void addAfterEventDateTime(String sleepNodeId, Instant afterTime) {
        Map<String, Instant> after = new HashMap<>();
        after.put("afterEventDateTime", afterTime);
        dynamicSleepNode.put(sleepNodeId, after);
    }

}
