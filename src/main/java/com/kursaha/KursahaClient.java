package com.kursaha;

import com.google.gson.Gson;
import com.kursaha.engagedatadrive.client.EngageDataDriveClient;
import com.kursaha.engagedatadrive.client.EngageDataDriveClientImpl;
import com.kursaha.mailkeets.client.MailkeetsClient;
import com.kursaha.mailkeets.client.MailkeetsClientImpl;
import com.kursaha.smartassist.client.SmartAssistClient;
import com.kursaha.smartassist.client.SmartAssistClientImpl;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.*;

/**
 * Client to hold various services
 */
public class KursahaClient {
    private static final String MAILKEETS_BASE_URL = "https://mailkeets.kursaha.com/api/";
    private static final String EDD_BASE_URL = "https://edd.kursaha.com/api/";
    private static final String SA_BASE_URL = "https://sa.kursaha.com/api/";

    /**
     * Mailkeets client
     */
    public final MailkeetsClient mk;

    /**
     * Smart Assist client
     */
    public final SmartAssistClient sa;

    /**
     * Kursaha client
     */
    public final EngageDataDriveClient edd;

    private final ScheduledExecutorService executorService;

    /**
     * Constructor
     *
     * @param apiKey string key
     */
    public KursahaClient(String apiKey) {
        this(apiKey, 1);
    }

    /**
     * Constructor
     * @param apiKey string key
     * @param nThread Number of threads use to execute the request
     */
    public KursahaClient(String apiKey, int nThread) {
        this(apiKey, EDD_BASE_URL, MAILKEETS_BASE_URL, SA_BASE_URL, nThread);
    }

    /**
     * Constructor
     *
     * @param apiKey string key
     */
    KursahaClient(String apiKey, String eddBaseUrl, String mailkeetsBaseUrl, String saBaseUrl, int nThread) {
        Gson gson = new Gson();
        this.executorService = Executors.newScheduledThreadPool(nThread);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.NONE);
        Dispatcher dispatcher = new Dispatcher(executorService);

        OkHttpClient okHttpClient =
                new OkHttpClient.Builder()
                        .connectTimeout(Duration.ofMinutes(1))
                        .callTimeout(Duration.ofMinutes(1))
                        .readTimeout(Duration.ofMinutes(1))
                        .dispatcher(dispatcher)
                        .addInterceptor(interceptor).build();

        this.mk = new MailkeetsClientImpl(new Credentials(apiKey), gson, mailkeetsBaseUrl, okHttpClient);
        this.edd = new EngageDataDriveClientImpl(new Credentials(apiKey), gson, eddBaseUrl, okHttpClient);
        this.sa = new SmartAssistClientImpl(new Credentials(apiKey), gson, saBaseUrl, okHttpClient);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println(Instant.now() +  ": Checking if there is any message are pending.");
            try {
                while (this.edd.hasSignals()) {
                    Thread.sleep(5_000L);
                }

                while (dispatcher.queuedCallsCount() != 0 && dispatcher.runningCallsCount() != 0) {
                    System.out.println("Queue message count: " + dispatcher.queuedCallsCount());
                    System.out.println("Running message count: " + dispatcher.runningCallsCount());
                    Thread.sleep(5_000L);
                }
                KursahaClient.this.executorService.shutdown();
                boolean status = KursahaClient.this.executorService.awaitTermination(30, TimeUnit.SECONDS);
                if(status) {
                    System.out.println(Instant.now() +  ": All messages sent successfully.");
                } else {
                    System.err.println(Instant.now() +  ": Warning! Few messages might be dropped.");
                }
            } catch (InterruptedException e) {
                // ignore me.
            }
            System.out.println(Instant.now() +  ": Ready to shutdown Kursaha client");
        }));
    }

}
