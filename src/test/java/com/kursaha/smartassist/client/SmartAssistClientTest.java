package com.kursaha.smartassist.client;

import com.kursaha.KursahaClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

class SmartAssistClientTest {

    @Test
    @Disabled
    void getResponse() throws IOException {
        KursahaClient kk = new KursahaClient("<API Key>");
        kk.sa.getResponse(UUID.fromString("<Chat Identifier>"), "How to contact customer support", new ArrayList<>());
    }
}