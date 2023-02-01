package com.kursaha.mailkeets.exception;

import com.kursaha.mailkeets.dto.ErrorMessageDto;
import lombok.Getter;

public class MailkeetsSendMailException extends Exception{
    @Getter
    private ErrorMessageDto errorMessageDto;
    public MailkeetsSendMailException(Exception e) {
        super(e);
    }

    public MailkeetsSendMailException(ErrorMessageDto errorMessageDto) {
        super(errorMessageDto.getMessage());
        this.errorMessageDto = errorMessageDto;
    }

    public MailkeetsSendMailException(String s) {
        super(s);
    }
}
