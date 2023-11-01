package com.kursaha.smartassist.client;

import com.kursaha.common.Callback;
import com.kursaha.smartassist.dto.ChatAutomationRequestDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Smart Assist client
 */
public interface SmartAssistClient {
    /**
     * Request to get the chat response
     * @param identifier to be used to map the Chat Automation on the Kursaha Platform
     * @param request string
     * @param historyQAndA previous chat
     * @param callback callback
     */
    void getResponse(
            UUID identifier,
            String request,
            List<ChatAutomationRequestDto.QAndA> historyQAndA,
            Callback<String> callback
    );

    /**
     * Request to get the chat response
     *
     * @param identifier chat identifier
     * @param request      string
     * @param historyQAndA previous chat
     * @return response
     * @throws IOException      if a problem occurred talking to the server.
     * @throws RuntimeException (and subclasses) if an unexpected error occurs creating the request or
     *                          decoding the response.
     */
    String getResponse(
            UUID identifier,
            String request,
            List<ChatAutomationRequestDto.QAndA> historyQAndA
    ) throws IOException;
}
