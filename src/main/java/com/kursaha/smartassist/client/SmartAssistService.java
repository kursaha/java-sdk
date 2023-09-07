package com.kursaha.smartassist.client;

import com.kursaha.smartassist.dto.ChatAutomationRequestDto;
import com.kursaha.smartassist.dto.ChatAutomationResponseDto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.UUID;

interface SmartAssistService {
    @POST("chat/{identifier}")
    @Headers({"Content-type: application/json"})
    Call<ChatAutomationResponseDto> getResponse(
            @Header("Authorization") String apiKey,
            @Path("identifier") UUID identifier,
            @Body ChatAutomationRequestDto dto
    );
}
