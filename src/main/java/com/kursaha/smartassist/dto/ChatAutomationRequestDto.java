package com.kursaha.smartassist.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

/**
 * Request payload for chat automation
 */
@Data
public class ChatAutomationRequestDto {
    // Identifier of the Chat Automation
    private final UUID identifier;
    // Current request
    private final String request;
    // Chat history
    private final History history;

    /**
     * History for the conversation
     */
    @Data
    public static class History {

        // previous Q and A
        private final List<QAndA> qAndAs;
    }

    /**
     * Previous Q and A
     */
    @Data
    @EqualsAndHashCode
    public static class QAndA {
        // request from user
        private String request;
        // response from the server
        private String response;
    }
}
