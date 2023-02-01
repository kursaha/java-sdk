package com.kursaha.engagedatadrive;

import com.kursaha.Credentials;

public class EngageDataDriveClient {
    private final String apiKey;

    public EngageDataDriveClient(Credentials credentials) {
        this.apiKey = credentials.getApiKey();
    }
}
