package com.kursaha.mailkeets.client;

import com.kursaha.mailkeets.dto.VerifiedDomainNameResponseDto;
import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.dto.MailResponseDto;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

/**
 * Mailkeets service
 */
interface MailkeetsService {

    /**
     * Send mail
     * @param requestDto request dto
     * @param apiKey api key
     * @return call to response
     */
    @POST("mail-send")
    @Headers({"Content-type: application/json"})
    Call<MailResponseDto> sendMail(
            @Body MailRequestDto requestDto,
            @Header("Authorization") String apiKey
    );

    /**
     * Get all verified domain
     * @param apiKey api key
     * @return list of all verified domain
     */
    @GET("sender-identities/verified-domains")
    @Headers({"Content-type: application/json"})
    Call<List<VerifiedDomainNameResponseDto>> getVerifiedDomains(
            @Header("Authorization") String apiKey
    );
}
