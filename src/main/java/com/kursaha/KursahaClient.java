package com.kursaha;

import com.kursaha.engagedatadrive.EngageDataDriveClient;
import com.kursaha.mailkeets.MailkeetsClient;
import com.kursaha.mailkeets.exception.MailkeetsSendMailException;

public class KursahaClient {

    public final MailkeetsClient mk;

    public final EngageDataDriveClient edd;

    public KursahaClient(String apiKey) {
        this.mk = new MailkeetsClient(apiKey);
        this.edd = new EngageDataDriveClient(apiKey);
    }

    public static void main(String[] args) throws MailkeetsSendMailException {
        String apiKey = ""; //Please enter your api key to send email
        KursahaClient kursahaClient = new KursahaClient(apiKey);
        System.out.println(kursahaClient.mk.send("hello", "a@d.com", "d@k.c", "Anupal", "Hello world"));
    }
}
