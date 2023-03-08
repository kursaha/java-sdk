package com.kursaha.mailkeets;

import com.kursaha.mailkeets.client.MailkeetsClient;
import com.kursaha.mailkeets.dto.MailRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KursahaClientTest {
    private final MailkeetsClient mailkeetsClient = mock(MailkeetsClient.class);
    private final String requestIdentifier = UUID.randomUUID().toString();
    private MailRequestDto requestDto = null;

    private void prepare() {
        requestDto = new MailRequestDto(
                "John",
                "john@doe.com",
                "hello@test.com",
                "This is test subject",
                "This is test body",
                requestIdentifier
        );
    }
    private void prepareError() {
        requestDto = new MailRequestDto(
                null,
                "john@doe.com",
                "hello@test.com",
                "This is test subject",
                "This is test body",
                requestIdentifier
        );
    }

    @Test
    public void sendMailTest () {
        prepare();
        // mock
        when(mailkeetsClient.sendMail(requestDto)).thenReturn(requestIdentifier);
        // play
        String identifier = mailkeetsClient.sendMail(requestDto);
        // check
        Assertions.assertNotNull(identifier);
        Assertions.assertEquals(this.requestIdentifier, identifier);
    }

    @Test
    public void sendMailTestErrorNullRequestParam () {
        assertThrows(NullPointerException.class, this::prepareError);
    }

}
