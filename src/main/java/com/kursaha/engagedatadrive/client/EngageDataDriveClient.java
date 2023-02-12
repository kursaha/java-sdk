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
     * @param eventflowId id of the whole event
     * @param stepNodeId id of the step event
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
     * @param eventFlowRequestDto object of EventFlowRequestDto
     * @throws EddException if error happens
     */

    void sendEventFlow(EventFlowRequestDto eventFlowRequestDto) throws EddException;
}
