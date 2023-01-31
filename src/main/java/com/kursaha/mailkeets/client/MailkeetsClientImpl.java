package com.kursaha.mailkeets.client;

import com.google.gson.Gson;
import com.kursaha.mailkeets.dto.ErrorMessageDto;
import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.dto.MailResponseDto;
import com.kursaha.mailkeets.exception.MailkeetsSendMailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class MailkeetsClientImpl implements MailkeetsClient {
    private static final String BASE_URL = "https://kursaha.com/api/";

    private final Logger log = LoggerFactory.getLogger(MailkeetsClientImpl.class);

    private final String apiKey;
    private final MailkeetsService service;


    public MailkeetsClientImpl(String apiKey) {
        this.apiKey = apiKey;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
         service = retrofit.create(MailkeetsService.class);
    }

    @Override
    public MailResponseDto send (
            String subject,
            String to,
            String from,
            String fromName,
            String body
    ) throws MailkeetsSendMailException {
        MailRequestDto requestDto = new MailRequestDto();
        requestDto.setTo(to);
        requestDto.setBody(body);
        requestDto.setSubject(subject);
        requestDto.setFromAddress(from);
        requestDto.setFromName(fromName);

        return sendMail(requestDto);
    }

    @Override
    public MailResponseDto sendMail(MailRequestDto requestDto) throws MailkeetsSendMailException {
        try {

            Call<MailResponseDto> repos = service.sendMail(requestDto, "Bearer " + apiKey);
            Response<MailResponseDto> response = repos.execute();

            if (!response.isSuccessful()) {
                Gson gson = new Gson();
                try {
                    ErrorMessageDto errorResponse = gson.fromJson(response.errorBody().charStream(), ErrorMessageDto.class);
                    log.error("Failed to send email : {}", errorResponse);
                } catch (Exception ex) {

                }
                throw new MailkeetsSendMailException();
            }

            return response.body();
        } catch (IOException | MailkeetsSendMailException e) {
            throw new MailkeetsSendMailException(e);
        }
    }
}
