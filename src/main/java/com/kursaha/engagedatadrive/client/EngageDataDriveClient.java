package com.kursaha.engagedatadrive.client;

import com.kursaha.engagedatadrive.dto.SignalMailPayload;
import com.kursaha.engagedatadrive.exception.EddException;

/**
 * Client for engage data drive
 */
public interface EngageDataDriveClient {

    /**
     * run eventFlow on engage data drive
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
     * Method is used to signal mail event if query in not provided in the event flow node in the graph
     *
     * @param eventflowId    id of the event flow
     * @param stepNodeId     id of the step event
     * @param emitterId      unique Id of the user
     * @param signalMailPayloadData mail data
     * @throws EddException if fails
     */
    void signalMail(
            Long eventflowId,
            String stepNodeId,
            String emitterId,
            SignalMailPayload signalMailPayloadData
    ) throws EddException;
}
