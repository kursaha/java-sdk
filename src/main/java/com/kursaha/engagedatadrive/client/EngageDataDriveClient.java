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
     * @param userId unique Id of the user
     * @param stepNodeId id of the event
     * @param data payload
     * @throws EddException if fails
     */
    void sendEventFlow(
            Long eventflowId,
            String userId,
            String stepNodeId,
            JsonObject data
    ) throws EddException;

    /**
     * run eventFlow on engage data drive
     *
     * @param eventflowId
     * @param eventFlowRequestDto object of EventFlowRequestDto
     * @return
     * @throws EddException
     */

    void sendEventFlow(Long eventflowId, EventFlowRequestDto eventFlowRequestDto) throws EddException;
}
