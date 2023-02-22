package com.kursaha.engagedatadrive.client;

import com.kursaha.engagedatadrive.dto.SignalMailPayload;
import com.kursaha.engagedatadrive.dto.SignalMessagePayload;
import com.kursaha.engagedatadrive.dto.StartEventPayload;
import com.kursaha.engagedatadrive.exception.EddException;
import java.util.UUID;

/**
 * Client for engage data drive
 */
public interface EngageDataDriveClient {

    /**
     * run eventFlow on engage data drive by id
     *
     * @param eventflowId id of the whole event
     * @param stepNodeId  id of the step event
     * @param emitterId   unique Id of the user
     * @throws EddException if fails
     */
    void signal(
            Long eventflowId,
            String stepNodeId,
            String emitterId
    ) throws EddException;


    /**
     * Method is used to signal a mail-event-flow if query in not provided in the event flow node in the graph
     *
     * @param eventflowId    id of the event flow
     * @param stepNodeId     id of the step event
     * @param emitterId      unique Id of the user
     * @param payload        mail data
     * @throws EddException if fails
     */
    void signal(
            Long eventflowId,
            String stepNodeId,
            String emitterId,
            SignalMailPayload payload
    ) throws EddException;

    /**
     * Method is used to start an event-flow if query in not provided in the event flow node in the graph
     *
     * @param eventflowId    id of the event flow
     * @param stepNodeId     id of the step event
     * @param emitterId      unique Id of the user
     * @param payload mail data
     * @throws EddException if fails
     */
    void signal(
            Long eventflowId,
            String stepNodeId,
            String emitterId,
            StartEventPayload payload
    ) throws EddException;

    /**
     * Method is used to start a message-event-flow if query in not provided in the event flow node in the graph
     *
     * @param eventflowId    id of the event flow
     * @param stepNodeId     id of the step event
     * @param emitterId      unique Id of the user
     * @param payload        message data
     * @throws EddException if fails
     */
    void signal(
            Long eventflowId,
            String stepNodeId,
            String emitterId,
            SignalMessagePayload payload
    ) throws EddException;

    /**
     * run eventFlow on engage data drive by identifier
     *
     * @param identifier id of the whole event
     * @param stepNodeId  id of the step event
     * @param emitterId   unique Id of the user
     * @throws EddException if fails
     */
    void signal(
            UUID identifier,
            String stepNodeId,
            String emitterId
    ) throws EddException;

    /**
     * Method is used to signal a mail-event-flow if query in not provided in the event flow node in the graph
     *
     * @param identifier    identifier of the event flow
     * @param stepNodeId     id of the step event
     * @param emitterId      unique Id of the user
     * @param payload        mail data
     * @throws EddException if fails
     */
    void signal(
            UUID identifier,
            String stepNodeId,
            String emitterId,
            SignalMailPayload payload
    ) throws EddException;

    /**
     * Method is used to start an event-flow if query in not provided in the event flow node in the graph
     *
     * @param identifier    identifier of the event flow
     * @param stepNodeId     id of the step event
     * @param emitterId      unique Id of the user
     * @param payload mail data
     * @throws EddException if fails
     */
    void signal(
            UUID identifier,
            String stepNodeId,
            String emitterId,
            StartEventPayload payload
    ) throws EddException;

    /**
     * Method is used to start a message-event-flow if query in not provided in the event flow node in the graph
     *
     * @param identifier    identifier of the event flow
     * @param stepNodeId     id of the step event
     * @param emitterId      unique Id of the user
     * @param payload        message data
     * @throws EddException if fails
     */
    void signal(
            UUID identifier,
            String stepNodeId,
            String emitterId,
            SignalMessagePayload payload
    ) throws EddException;
}
