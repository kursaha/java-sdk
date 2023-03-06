package com.kursaha;

import com.google.gson.Gson;
import com.kursaha.engagedatadrive.client.EngageDataDriveClient;
import com.kursaha.engagedatadrive.client.EngageDataDriveClientImpl;
import com.kursaha.mailkeets.client.MailkeetsClient;
import com.kursaha.mailkeets.client.MailkeetsClientImpl;

import java.util.concurrent.*;

/**
 * Client to hold various services
 */
public class KursahaClient {
    private static final String MAILKEETS_BASE_URL = "https://mailkeets.kursaha.com/api/";
    private static final String EDD_BASE_URL = "https://edd.kursaha.com/api/";

    /**
     * Mailkeets client
     */
    public final MailkeetsClient mk;

    /**
     * Kursaha client
     */
    public final EngageDataDriveClient edd;

    /**
     * Number of threads use to execute the request default to 1
     */
    private final int numThreadExecutor;

    private final ScheduledExecutorService executor;

    private final Gson gson;

    /**
     * Constructor
     *
     * @param apiKey string key
     */
    public KursahaClient(String apiKey) {
        this(apiKey, 1);
    }

    public KursahaClient(String apiKey, int nThread) {
        this(apiKey, EDD_BASE_URL, MAILKEETS_BASE_URL, nThread);
    }

    /**
     * Constructor
     *
     * @param apiKey string key
     */
    KursahaClient(String apiKey, String eddBaseUrl, String mailkeetsBaseUrl, int nThread) {
        this.numThreadExecutor = nThread;
        this.gson = new Gson();
        this.executor = Executors.newScheduledThreadPool(nThread);
        this.mk = new MailkeetsClientImpl(new Credentials(apiKey), gson, mailkeetsBaseUrl,executor);
        this.edd = new EngageDataDriveClientImpl(new Credentials(apiKey), gson, eddBaseUrl, executor);
    }

    public void awaitTermination(long waitInMs) throws InterruptedException {
        this.executor.awaitTermination(waitInMs, TimeUnit.MILLISECONDS);
    }
}
