package com.kursaha.mailkeets;

public class MailkeetsClient {
    private final String apiKey;

    public MailkeetsClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public void send(String subject, String to, String from, String fromName, String contentType, String contentBody) {
        throw new RuntimeException("not implemented");
    }
}
