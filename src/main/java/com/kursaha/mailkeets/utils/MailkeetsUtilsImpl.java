package com.kursaha.mailkeets.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kursaha.mailkeets.dto.ErrorMessageDto;
import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.exception.MailkeetsSendMailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class MailkeetsUtilsImpl {

    private final Logger log = LoggerFactory.getLogger(MailkeetsUtilsImpl.class);
    private final String baseUrl = "http://localhost:8081/api/";
    public JsonObject sendMail(MailRequestDto requestDto, String apiKey) throws MailkeetsSendMailException {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MailkeetsUtils service = retrofit.create(MailkeetsUtils.class);
            Call<JsonObject> repos = service.sendMail(requestDto, "Bearer " + apiKey);
            Response<JsonObject> execute = repos.execute();
            log.info("Response for sending mail: {}", execute);

            if (!execute.isSuccessful()) {
                log.error("Failed to send mail");
                Gson gson = new Gson();
                ErrorMessageDto errorResponse = gson.fromJson(execute.errorBody().charStream(),ErrorMessageDto.class);
                log.error("error : {}", errorResponse);
                throw new MailkeetsSendMailException();
            }

            return execute.body();
        } catch (IOException | MailkeetsSendMailException e) {
            throw new MailkeetsSendMailException(e);
        }
    }

}
