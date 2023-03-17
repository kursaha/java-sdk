package com.kursaha.engagedatadrive.client;

import com.kursaha.engagedatadrive.dto.SignalMailPayload;
import com.kursaha.engagedatadrive.dto.SignalMessagePayload;
import com.kursaha.engagedatadrive.dto.StartEventPayload;

import java.util.UUID;

/**
 * Client for engage data drive
 */
public interface EngageDataDriveClient {
    /**
     * run eventFlow on engage data drive by identifier
     *
     * @param identifier id of the whole event
     * @param stepNodeId  id of the step event
     * @param emitterId   unique Id of the user
     */
    void signal(
            UUID identifier,
            String stepNodeId,
            String emitterId
    );

    /**
     * Method is used to signal a mail-event-flow if query is not provided in the event flow node in the graph
     *
     * @param identifier    identifier of the event flow
     * @param stepNodeId     id of the step event
     * @param emitterId      unique Id of the user
     * @param payload        mail data
     */
    void signal(
            UUID identifier,
            String stepNodeId,
            String emitterId,
            SignalMailPayload payload
    );

    /**
     * Method is used to start an event-flow if query is not provided in the event flow node in the graph
     *
     * @param identifier    identifier of the event flow
     * @param stepNodeId     id of the step event
     * @param emitterId      unique Id of the user
     * @param payload mail data
     */
    void signal(
            UUID identifier,
            String stepNodeId,
            String emitterId,
            StartEventPayload payload
    );

    /**
     * Method is used to start a message-event-flow if query is not provided in the event flow node in the graph
     *
     * @param identifier    identifier of the event flow
     * @param stepNodeId     id of the step event
     * @param emitterId      unique Id of the user
     * @param payload        message data
     */
    void signal(
            UUID identifier,
            String stepNodeId,
            String emitterId,
            SignalMessagePayload payload
    );

    /**
     *
     * @return true, if there are any pending messages in the sdk to process
     */
    boolean hasSignals();
}
