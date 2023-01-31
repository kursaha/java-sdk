package com.kursaha.mailkeets.client;

import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.dto.MailResponseDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MailkeetsService {

    @POST("mail-send")
    @Headers({"Content-type: application/json"})
    Call<MailResponseDto> sendMail(
            @Body MailRequestDto requestDto,
            @Header("Authorization") String apiKey
    );
}
