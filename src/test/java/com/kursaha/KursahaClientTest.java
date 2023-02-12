package com.kursaha;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KursahaClientTest {

    /**
     * Create a unique api key from https://kursaha.com/engage-data-drive/api-key. After that, please add that api key
     * into system environment.
     */
    private final String apiKey = System.getenv("key"); // Name of the Environment variable
    private KursahaClient kursahaClient;

    private String emitterId;

    @BeforeEach
    public void init() {
        kursahaClient = new KursahaClient(apiKey);
        emitterId = "<UNIQUE_EMITTER_ID>";
    }

    @Test
    public void testSendLoginEvent() throws Exception {
        kursahaClient.edd.sendEventFlow(-1L /* WorkflowId */, "<Step_Node_Id>", new JsonObject() /* User Custom Payload*/, emitterId);
    }
}