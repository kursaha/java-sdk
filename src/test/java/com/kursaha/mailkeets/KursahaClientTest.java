package com.kursaha.mailkeets;

import com.kursaha.mailkeets.client.MailkeetsClient;
import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.dto.MailResponseDto;
import com.kursaha.mailkeets.exception.MailkeetsSendMailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KursahaClientTest {

    private MailkeetsClient mailkeetsClient = mock(MailkeetsClient.class);
    private final String apiKey = "12345";

    private final String traceId = UUID.randomUUID().toString();

    private MailRequestDto requestDto = null;

    @BeforeEach
    public void beforeEachTest() {
        requestDto = new MailRequestDto();
        requestDto.setTo("hello@a.com");
        requestDto.setFromName("John");
        requestDto.setBody("Hello");
        requestDto.setSubject("Hello World");
        requestDto.setFromAddress("john@k.com");
    }

    @Test
    public void sendMailTest () throws MailkeetsSendMailException {
        when(mailkeetsClient.sendMail(requestDto))
                .thenReturn(new MailResponseDto(traceId));

    }
}
