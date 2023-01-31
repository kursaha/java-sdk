package com.kursaha;

import com.kursaha.engagedatadrive.EngageDataDriveClient;
import com.kursaha.mailkeets.client.MailkeetsClientImpl;

public class KursahaClient {

    public final MailkeetsClientImpl mk;

    public final EngageDataDriveClient edd;

    public KursahaClient(String apiKey) {
        this.mk = new MailkeetsClientImpl(apiKey);
        this.edd = new EngageDataDriveClient(apiKey);
    }
}
