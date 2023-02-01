package com.kursaha.mailkeets;

import com.kursaha.mailkeets.client.MailkeetsClient;
import com.kursaha.mailkeets.dto.MailRequestDto;
import com.kursaha.mailkeets.dto.MailResponseDto;
import com.kursaha.mailkeets.exception.MailkeetsSendMailException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KursahaClientTest {
    private final MailkeetsClient mailkeetsClient = mock(MailkeetsClient.class);
    private final String traceId = UUID.randomUUID().toString();
    private MailRequestDto requestDto = null;

    private void prepare() {
        requestDto = new MailRequestDto(
                "John",
                "john@doe.com",
                "hello@test.com",
                "This is test subject",
                "This is test body"
        );
    }
    private void prepareError() {
        requestDto = new MailRequestDto(
                null,
                "john@doe.com",
                "hello@test.com",
                "This is test subject",
                "This is test body"
        );
    }

    @Test
    public void sendMailTest () throws MailkeetsSendMailException {
        prepare();
        // mock
        when(mailkeetsClient.sendMail(requestDto)).thenReturn(new MailResponseDto(traceId));
        // play
        MailResponseDto mailResponseDto = mailkeetsClient.sendMail(requestDto);
        // check
        Assertions.assertNotNull(mailResponseDto);
        Assertions.assertEquals(traceId, mailResponseDto.getTraceId());
    }

    @Test
    public void sendMailTestErrorNullRequestParam () {
        assertThrows(NullPointerException.class, this::prepareError);
    }

}
