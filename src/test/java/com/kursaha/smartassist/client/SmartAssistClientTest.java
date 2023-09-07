package com.kursaha.smartassist.client;

import com.kursaha.KursahaClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SmartAssistClientTest {

    @Test
    @Disabled
    void getResponse() throws IOException {
        KursahaClient kk = new KursahaClient("<API Key>");
        kk.sa.getResponse("How to contact customer support", null);
    }
}