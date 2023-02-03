package com.kursaha;

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
        this.mk = new MailkeetsClientImpl(new Credentials(apiKey));
        this.edd = null;
    }

    /**
     *
     * @param apiKey string key
     * @param id Engage data drive eventFlow id
     */

    public KursahaClient(String apiKey, String id) {
        this.mk = null;
        this.edd = new EngageDataDriveClientImpl(new Credentials(apiKey), id);
    }
}
