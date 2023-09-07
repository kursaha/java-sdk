package com.kursaha.engagedatadrive.client;

import com.kursaha.common.Callback;
import com.kursaha.engagedatadrive.dto.*;

import java.io.IOException;
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
     * Method is used to start a fcm-notification-event-flow if query is not provided in the event flow node in the graph
     * @param identifier  identifier of the event flow
     * @param stepNodeId  id of the step event
     * @param emitterId  unique Id of the user
     * @param payload  fcm notification data
     */
    void signal(
            UUID identifier,
            String stepNodeId,
            String emitterId,
            SignalFcmNotificationPayload payload
    );

    /**
     * Method is used to start a Interakt-whatsapp-event-flow if query is not provided in the event flow node in the graph
     * @param identifier  identifier of the event flow
     * @param stepNodeId  id of the step event
     * @param emitterId  unique Id of the user
     * @param payload  Interakt whatsapp data
     */
    void signal(
            UUID identifier,
            String stepNodeId,
            String emitterId,
            SignalInteraktWhatsappPayload payload
    );


    /**
     *
     * @return true, if there are any pending messages in the sdk to process
     */
    boolean hasSignals();

    /**
     *
     * @return true if able to connect to server with valid key
     * @throws IOException on network error
     */
    boolean isConnectedAndAuthenticated() throws IOException;

    /**
     * This method will create new customer data or update an existing customer
     * @param customerId Unique customer id
     * @param customerData Details of customer
     * @throws IOException on network error
     * @throws RuntimeException (and subclasses) if an unexpected error occurs creating the request or
     * decoding the response.
     */
    void sendCustomerData (
        String customerId,
        CustomerData customerData
    ) throws IOException;


    /**
     * This method will create new customer data or update an existing customer
     * The operation is performed in an async way
     * @param customerId Unique customer id
     * @param customerData Details of customer
     * @param callback to get notified on Success and Failure
     * decoding the response.
     */
    void sendCustomerData (
            String customerId,
            CustomerData customerData,
            Callback callback
    );

    /**
     * This method will send event data to Kursaha
     * @param customerId Unique customer id
     * @param eventType Types of event such as view, checkout
     * @param eventData event specific data
     */
    void trace (
        String customerId,
        String eventType,
        EventData eventData
    );
}
