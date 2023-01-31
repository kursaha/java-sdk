package com.kursaha.mailkeets.utils;

import com.google.gson.JsonObject;
import com.kursaha.mailkeets.dto.MailRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MailkeetsUtils {

    @POST("mail-send")
    @Headers({"Content-type: application/json"})
    Call<JsonObject> sendMail(
            @Body MailRequestDto requestDto,
            @Header("Authorization") String apiKey
    );
}
