package com.kursaha;

import com.kursaha.engagedatadrive.EngageDataDriveClient;
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
     * @param apiKey string key
     */
    public KursahaClient(String apiKey) {
        this.mk = new MailkeetsClientImpl(new Credentials(apiKey));
        this.edd = new EngageDataDriveClient(new Credentials(apiKey));
    }
}
