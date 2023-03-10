package com.kursaha.mailkeets.client;

import com.google.gson.Gson;
import com.kursaha.common.ErrorMessageDto;
import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.dto.MailResponseDto;
import com.kursaha.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mailkeets client implementation
 */
public class MailkeetsClientImpl implements MailkeetsClient {
    private static final Logger LOGGER = Logger.getLogger(MailkeetsClientImpl.class.getName());

    private final String apiKey;
    private final MailkeetsService service;
    private final Gson gson;

    /**
     * Constructor
     *
     * @param credentials  credentials object
     * @param gson         json convertor
     * @param baseUrl      baseurl of api
     * @param okHttpClient OkHttp client
     */
    public MailkeetsClientImpl(Credentials credentials, Gson gson, String baseUrl, OkHttpClient okHttpClient) {
        this.apiKey = credentials.getApiKey();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MailkeetsService.class);
        this.gson = gson;
    }

    @Override
    public String send(
            String subject,
            String to,
            String from,
            String fromName,
            String body
    ) {
        String requestIdentifier = UUID.randomUUID().toString();
        MailRequestDto requestDto = new MailRequestDto(fromName, from, to, subject, body, requestIdentifier);
        return sendMail(requestDto);
    }

    @Override
    public String sendMail(MailRequestDto requestDto) {
        Call<MailResponseDto> repos = service.sendMail(requestDto, "Bearer " + apiKey);
        repos.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<MailResponseDto> call, Response<MailResponseDto> response) {
                if (!response.isSuccessful()) {
                    try {
                        ErrorMessageDto errorResponse = gson.fromJson(response.errorBody().charStream(), ErrorMessageDto.class);
                        LOGGER.log(Level.SEVERE, "failed to execute request", errorResponse);
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, "failed to execute request", ex);
                    }
                }

            }

            @Override
            public void onFailure(Call<MailResponseDto> call, Throwable t) {
                LOGGER.log(Level.SEVERE, "failed to execute request", t);
            }
        });
        return requestDto.getRequestIdentifier();
    }
}
