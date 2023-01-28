package com.kursaha;

import com.kursaha.engagedatadrive.EngageDataDriveClient;
import com.kursaha.mailkeets.MailkeetsClient;

public class KursahaClient {

    public final MailkeetsClient mk;

    public final EngageDataDriveClient edd;

    public KursahaClient(String apiKey) {
        this.mk = new MailkeetsClient(apiKey);
        this.edd = new EngageDataDriveClient(apiKey);
    }
}
