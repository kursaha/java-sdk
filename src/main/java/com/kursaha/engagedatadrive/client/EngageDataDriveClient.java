package com.kursaha.engagedatadrive.client;

import com.google.gson.JsonObject;
import com.kursaha.engagedatadrive.dto.EventFlowRequestDto;
import com.kursaha.engagedatadrive.exception.EddException;

/**
 * Client for engage data drive
 */
public interface EngageDataDriveClient {

    /**
     * run eventFlow on engage data drive
     * @param stepNodeId id of the event
     * @param data payload
     * @param emitterId unique Id of the user
     * @throws EddException if fails
     */
    void sendEventFlow(
            Long eventflowId,
            String stepNodeId,
            JsonObject data,
            String emitterId
    ) throws EddException;

    /**
     * run eventFlow on engage data drive
     *
     * @param eventflowId id of the event flow
     * @param eventFlowRequestDto object of EventFlowRequestDto
     * @throws EddException if error happens
     */

    void sendEventFlow(Long eventflowId, EventFlowRequestDto eventFlowRequestDto) throws EddException;
}
