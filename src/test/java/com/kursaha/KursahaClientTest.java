package com.kursaha;

import com.kursaha.engagedatadrive.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.io.IOException;
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
    @Disabled("use to test endpoints with identifier")
    public void testStartWorkflow() throws Exception {
        UUID identifier = UUID.fromString("EventFlow Identifier");
        for (int i = 0; i < 10; i++) {
            emitterId = "Ausvsa2bv2_n_" + i + 100100L;
            SignalMailPayload payload = new SignalMailPayload(emitterId + "@example.com");
            kursahaClient.edd.signal(identifier, "start_event", emitterId, payload);
        }
    }

    @Test
    @Disabled("use to test endpoints for sending customer data")
    public void sendCustomerDataTest() throws IOException {
        UUID customerId = UUID.randomUUID();
        CustomerData customerData = new CustomerData();
        customerData.setEmailId("as.,d@gd.com");
        kursahaClient.edd.sendCustomerData(customerId.toString(), customerData);
    }

    @Test
    @Disabled("use to test endpoints with identifier")
    public void testGetIdByIdentifier() throws InterruptedException {
        UUID identifier = UUID.fromString("<UUID of EventFlow>");
        SignalMailPayload payload = new SignalMailPayload("admin@example.com");
        kursahaClient.edd.signal(identifier /* EventFlow Identifier */, "<STEP_NODE_IO>", emitterId, payload);
    }

    @Test
    public void getAllVerifiedDomain() throws Exception {
        System.out.println(kursahaClient.mk.getVerifiedDomains());
    }
}
