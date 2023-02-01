package com.kursaha.mailkeets.client;

import com.google.gson.Gson;
import com.kursaha.mailkeets.dto.ErrorMessageDto;
import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.dto.MailResponseDto;
import com.kursaha.mailkeets.exception.MailkeetsSendMailException;
import com.kursaha.Credentials;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * Mailkeets client implementation
 */
public class MailkeetsClientImpl implements MailkeetsClient {
    private static final String BASE_URL = "https://kursaha.com/api/";
    private final String apiKey;
    private final MailkeetsService service;
    private final Gson gson;


    /**
     * Constructor
     * @param credentials credentials object
     */
    public MailkeetsClientImpl(Credentials credentials) {
        this.apiKey = credentials.getApiKey();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MailkeetsService.class);
        gson = new Gson();
    }

    @Override
    public MailResponseDto send(
            String subject,
            String to,
            String from,
            String fromName,
            String body
    ) throws MailkeetsSendMailException {
        MailRequestDto requestDto = new MailRequestDto(fromName, fromName, to, subject, body);
        return sendMail(requestDto);
    }

    @Override
    public MailResponseDto sendMail(MailRequestDto requestDto) throws MailkeetsSendMailException {
        try {
            Call<MailResponseDto> repos = service.sendMail(requestDto, "Bearer " + apiKey);
            Response<MailResponseDto> response = repos.execute();
            if (!response.isSuccessful()) {
                try {
                    ErrorMessageDto errorResponse = gson.fromJson(response.errorBody().charStream(), ErrorMessageDto.class);
                    throw new MailkeetsSendMailException(errorResponse);
                } catch (Exception ex) {
                    throw new MailkeetsSendMailException(ex);
                }
            }
            return response.body();
        } catch (IOException | MailkeetsSendMailException e) {
            throw new MailkeetsSendMailException(e);
        }
    }
}
