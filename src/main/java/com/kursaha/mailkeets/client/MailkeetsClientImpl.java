package com.kursaha.mailkeets.client;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.kursaha.Credentials;
import com.kursaha.common.ErrorMessageDto;
import com.kursaha.common.KursahaException;
import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.dto.MailResponseDto;
import com.kursaha.mailkeets.dto.VerifiedDomainNameResponseDto;
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
 * Mailkeets client implementation
 */
public class MailkeetsClientImpl implements MailkeetsClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailkeetsClientImpl.class.getName());

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
            String contentType,
            String body,
            String unsubscribedList
    ) throws KursahaException, IOException {
        UUID requestIdentifier = UUID.randomUUID();
        MailRequestDto requestDto = new MailRequestDto(fromName, from, to, subject, contentType, body, requestIdentifier, unsubscribedList);
        return sendMail(requestDto);
    }

    @Override
    public String sendMail(MailRequestDto requestDto) throws KursahaException, IOException {
        Call<MailResponseDto> repos = service.sendMail(requestDto, "Bearer " + apiKey);
        Response<MailResponseDto> response = repos.execute();
        if (!response.isSuccessful()) {
            try {
                ErrorMessageDto errorMessageDto = gson.fromJson(response.errorBody().charStream(), ErrorMessageDto.class);
                LOGGER.error("failed to execute request : {}", errorMessageDto);
                throw new KursahaException(errorMessageDto);
            } catch (JsonIOException | JsonSyntaxException je) {
                throw new RuntimeException(je);
            }
        }
        return requestDto.getRequestIdentifier().toString();
    }

    @Override
    public List<VerifiedDomainNameResponseDto> getVerifiedDomains() throws Exception {
        ErrorMessageDto errorResponse = new ErrorMessageDto();
        try {
            Call<List<VerifiedDomainNameResponseDto>> repos = service.getVerifiedDomains("Bearer " + apiKey);
            Response<List<VerifiedDomainNameResponseDto>> response = repos.execute();
            if (!response.isSuccessful()) {
                String responseAsString = null;
                StringBuilder sb = new StringBuilder();
                for (int ch; (ch = response.errorBody().charStream().read()) != -1; ) {
                    sb.append((char) ch);
                }
                responseAsString = sb.toString();
                errorResponse = gson.fromJson(responseAsString, ErrorMessageDto.class);
                LOGGER.error("failed to execute request : {}", errorResponse);
            }
            return response.body();
        } catch (Exception e) {
            LOGGER.error("failed to execute request : {}", errorResponse);
            throw e;
        }
    }
}
