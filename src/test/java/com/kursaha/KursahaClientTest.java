package com.kursaha;

import com.kursaha.engagedatadrive.dto.SignalMailPayload;
import com.kursaha.engagedatadrive.exception.EddException;
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
        emitterId = "<UNIQUE_EMITTER_ID>";
    }

    @Test
    @Disabled("use to test endpoints")
    public void testStartWorkflow() throws Exception {
        kursahaClient.edd.signal(UUID.randomUUID() /* WorkflowIdentifier */, "<STEP_NODE_IO>", emitterId);
    }

    @Test
    @Disabled("use to test endpoints with identifier")
    public void testGetIdByIdentifier() throws EddException {
        UUID identifier = UUID.fromString("<UUID of eventFlow>");
        SignalMailPayload payload = new SignalMailPayload("example@mail.com");
        kursahaClient.edd.signal(identifier /* EventFlow Identifier */, "<STEP_NODE_ID>", emitterId, payload);
    }
}