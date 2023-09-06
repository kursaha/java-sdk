package com.kursaha.common;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class RetryInterceptor implements Interceptor {
    private final int maxRetries;

    public RetryInterceptor(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;

        // Perform retries if an IOException occurs
        for (int retry = 1; retry <= maxRetries; retry++) {
            try {
                response = chain.proceed(request);
                if (response.isSuccessful()) {
                    return response;
                }
            } catch (IOException e) {
                if (retry >= maxRetries) {
                    throw e; // No more retries, throw the exception
                } else {
                    try {
                        Thread.sleep(retry * 5000L);
                    } catch (InterruptedException ex) {
                        // skip
                    }
                }
            }
        }

        return response; // Return the last response (either success or failure)
    }
}

