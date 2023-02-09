package com.kursaha;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KursahaClientTest {

    private final String apiKey = System.getenv("key");
    private KursahaClient kursahaClient;

    private String emitterId;

    @BeforeEach
    public void init() {
        kursahaClient = new KursahaClient(apiKey, "http://localhost:8082/api/", "http://localhost:8081/api/");
        emitterId = "123";
    }

    @Test
    public void testSendLoginEvent() throws Exception {
        kursahaClient.edd.sendEventFlow(13L, "start_event", new JsonObject(), emitterId);
    }
}