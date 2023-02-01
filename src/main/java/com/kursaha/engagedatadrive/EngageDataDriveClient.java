package com.kursaha.engagedatadrive;

import com.kursaha.Credentials;

/**
 * Client for engage data drive
 */
public class EngageDataDriveClient {
    private final String apiKey;

    /**
     * Constructor
     * @param credentials credentials object
     */
    public EngageDataDriveClient(Credentials credentials) {
        this.apiKey = credentials.getApiKey();
    }
}
