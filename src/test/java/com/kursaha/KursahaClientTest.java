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
    private final String apiKey = System.getenv("key");
    private KursahaClient kursahaClient;

    private String emitterId;

    @BeforeEach
    public void init() {
        kursahaClient = new KursahaClient(apiKey);
        emitterId = UUID.randomUUID().toString();
    }

    @Test
    public void testStartWorkflow() throws Exception {
        UUID identifier = UUID.fromString("abdd6d04-c9f6-448c-9d26-4dc0d9e8b7e2");

        for (int i = 0; i < 5000; i++) {
            emitterId = "Ausvsa2bv2_n_" + i;
            SignalMailPayload payload = new SignalMailPayload(emitterId + "@example.com");
            kursahaClient.edd.signal(identifier, "start_event", emitterId, payload);
        }
    }

    @Test
    @Disabled("use to test endpoints with identifier")
    public void testGetIdByIdentifier() throws InterruptedException {
        UUID identifier = UUID.fromString("<UUID of EventFlow>");
        SignalMailPayload payload = new SignalMailPayload("admin@example.com");
        kursahaClient.edd.signal(identifier /* EventFlow Identifier */, "<STEP_NODE_IO>", emitterId, payload);
    }
}