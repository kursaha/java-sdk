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
     * @param emitterId customer id of your user
     * @param stepNodeId
     * @param data
     * @return
     * @throws EddException
     */
    Void sendEventFlow(
            String emitterId,
            String stepNodeId,
            JsonObject data
    ) throws EddException;

    /**
     * run eventFlow on engage data drive
     * @param eventFlowRequestDto object of EventFlowRequestDto
     * @return
     * @throws EddException
     */

    Void sendEventFlow(EventFlowRequestDto eventFlowRequestDto) throws EddException;
}
