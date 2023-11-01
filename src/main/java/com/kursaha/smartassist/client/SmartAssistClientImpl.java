package com.kursaha.smartassist.client;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.kursaha.Credentials;
import com.kursaha.common.Callback;
import com.kursaha.common.ErrorMessageDto;
import com.kursaha.common.KursahaException;
import com.kursaha.smartassist.dto.ChatAutomationRequestDto;
import com.kursaha.smartassist.dto.ChatAutomationRequestDto.History;
import com.kursaha.smartassist.dto.ChatAutomationResponseDto;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Smart Assist client implementation
 */
public class SmartAssistClientImpl implements SmartAssistClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmartAssistClientImpl.class.getName());
    private final String apiKey;
    private final Gson gson;
    private final SmartAssistService service;


    /**
     * Smart Assist client implementation
     * @param credentials API credentials
     * @param gson gson
     * @param baseUrl base url
     * @param okHttpClient http client
     */
    public SmartAssistClientImpl(Credentials credentials, Gson gson, String baseUrl, OkHttpClient okHttpClient) {
        this.apiKey = credentials.getApiKey();
        this.gson = gson;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        this.service = retrofit.create(SmartAssistService.class);
    }

    @Override
    public String getResponse(
            UUID identifier,
            String request,
            List<ChatAutomationRequestDto.QAndA> historyQAndA
    ) throws IOException {
        ChatAutomationRequestDto dto = new ChatAutomationRequestDto(identifier, request, new History(historyQAndA));
        Call<ChatAutomationResponseDto> chatResponse = service.getResponse("Bearer " + apiKey, identifier, dto);
        Response<ChatAutomationResponseDto> response = chatResponse.execute();
        if (!response.isSuccessful()) {
            try {
                ErrorMessageDto errorMessageDto = gson.fromJson(response.errorBody().charStream(), ErrorMessageDto.class);
                LOGGER.error("failed to execute request : {}", errorMessageDto);
                throw new KursahaException(errorMessageDto);
            } catch (JsonIOException | JsonSyntaxException | KursahaException je) {
                throw new RuntimeException(je);
            }
        }
        return response.body().getResponse();
    }

    @Override
    public void getResponse(
            UUID identifier,
            String request,
            List<ChatAutomationRequestDto.QAndA> historyQAndA,
            Callback<String> callback
    ) {
        ChatAutomationRequestDto dto = new ChatAutomationRequestDto(identifier, request, new History(historyQAndA));
        Call<ChatAutomationResponseDto> response = service.getResponse("Bearer " + apiKey, identifier, dto);
        response.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(Call<ChatAutomationResponseDto> call, Response<ChatAutomationResponseDto> response) {
                try {
                    if (response.isSuccessful()) {
                        ChatAutomationResponseDto responseDto = response.body();
                        callback.onSuccess(responseDto.getResponse());
                    }
                } catch (Exception ex) {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ChatAutomationResponseDto> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

}
