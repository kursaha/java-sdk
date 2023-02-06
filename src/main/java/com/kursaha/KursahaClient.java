package com.kursaha;

import com.google.gson.Gson;
import com.kursaha.engagedatadrive.client.EngageDataDriveClient;
import com.kursaha.engagedatadrive.client.EngageDataDriveClientImpl;
import com.kursaha.mailkeets.client.MailkeetsClient;
import com.kursaha.mailkeets.client.MailkeetsClientImpl;

/**
 * Client to hold various services
 */
public class KursahaClient {

    /**
     * Mailkeets client
     */
    public final MailkeetsClient mk;

    /**
     * Kursaha client
     */
    public final EngageDataDriveClient edd;

    /**
     * Constructor
     *
     * @param apiKey string key
     */
    public KursahaClient(String apiKey) {
        Gson gson = new Gson();
        this.mk = new MailkeetsClientImpl(new Credentials(apiKey), gson);
        this.edd = new EngageDataDriveClientImpl(new Credentials(apiKey), gson);
    }
}
