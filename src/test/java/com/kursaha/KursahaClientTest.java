package com.kursaha;

import com.kursaha.engagedatadrive.dto.SignalMailPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.UUID;

class KursahaClientTest {

    /**
     * Create a unique api key from <a href="https://kursaha.com/engage-data-drive/api-key">API Key</a>.
     * After that, please add that api key into system environment.
     */
    private final String apiKey = System.getenv("key"); // Name of the Environment variable
    private KursahaClient kursahaClient;

    private String mailkeetsBaseUrl = "http://localhost:8081/api/";
    private String eddBaseUrl = "http://localhost:8082/api/";

    private String emitterId;

    @BeforeEach
    public void init() {
        kursahaClient = new KursahaClient(apiKey, eddBaseUrl, mailkeetsBaseUrl, 3);
        emitterId = UUID.randomUUID().toString();
    }

    @Test
    @Disabled("use to test endpoints")
    public void testStartWorkflow() throws Exception {
        kursahaClient.edd.signal(UUID.randomUUID() /* WorkflowIdentifier */, "<STEP_NODE_IO>", emitterId);
    }

    @Test
//    @Disabled("use to test endpoints with identifier")
    public void testGetIdByIdentifier() throws InterruptedException {
        for (int i=1; i<=5000; i++) {
            emitterId = "Customer " + i;
            UUID identifier = UUID.fromString("8a8d0206-2b1d-4433-9549-2d1c50857ac2");
            SignalMailPayload payload = new SignalMailPayload(emitterId + "@example.com");
            payload.addProperty("name", emitterId);
            kursahaClient.edd.signal(identifier /* EventFlow Identifier */, "start_event", emitterId, payload);
//            if(i % 5 == 0) {
////                Thread.sleep(10);
////            }
        }
    }

    @Test
    public void testGetIdByIdentifier1() throws InterruptedException {
        for (int i=101; i<=200; i++) {
            emitterId = "Customer " + i;
            UUID identifier = UUID.fromString("8a8d0206-2b1d-4433-9549-2d1c50857ac2");
            SignalMailPayload payload = new SignalMailPayload(emitterId + "@example.com");
            payload.addProperty("name", emitterId);
            kursahaClient.edd.signal(identifier /* EventFlow Identifier */, "start_event", emitterId, payload);
            if(i % 5 == 0) {
                Thread.sleep(1000);
            }
        }
    }
}