package com.kursaha;

import com.kursaha.engagedatadrive.EngageDataDriveClient;
import com.kursaha.mailkeets.client.MailkeetsClient;
import com.kursaha.mailkeets.client.MailkeetsClientImpl;

public class KursahaClient {

    public final MailkeetsClient mk;

    public final EngageDataDriveClient edd;

    public KursahaClient(String apiKey) {
        this.mk = new MailkeetsClientImpl(new Credentials(apiKey));
        this.edd = new EngageDataDriveClient(new Credentials(apiKey));
    }
}
