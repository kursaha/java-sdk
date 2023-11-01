package com.kursaha.smartassist.client;

import com.kursaha.KursahaClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

class SmartAssistClientTest {

    @Test
//    @Disabled
    void getResponse() throws IOException {
        KursahaClient kk = new KursahaClient("<API Key>");
        kk.sa.getResponse(UUID.fromString("6b9a2f9d-915d-4061-b9f3-1c8a4cbcdf55"), "How to contact customer support", new ArrayList<>());
    }
}