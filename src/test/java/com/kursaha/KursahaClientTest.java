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

    private String emitterId;

    @BeforeEach
    public void init() {
        kursahaClient = new KursahaClient(apiKey);
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
        UUID identifier = UUID.fromString("abdd6d04-c9f6-448c-9d26-4dc0d9e8b7e2");
        SignalMailPayload payload = new SignalMailPayload("admin@nishant.life");
        kursahaClient.edd.signal(identifier /* EventFlow Identifier */, "start_event", emitterId, payload);
        kursahaClient.awaitTermination(10_000L);
    }
}