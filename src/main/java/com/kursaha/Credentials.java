package com.kursaha;

import lombok.Getter;

/**
 * Credentials file
 */
public class Credentials {
    @Getter
    private final String apiKey;

    Credentials(String apiKey) {
        this.apiKey = apiKey;
    }
}
