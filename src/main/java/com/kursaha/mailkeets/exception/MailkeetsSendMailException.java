package com.kursaha.mailkeets.exception;

import com.kursaha.mailkeets.dto.ErrorMessageDto;
import lombok.Getter;

/**
 * Mailkeets exception
 */
public class MailkeetsSendMailException extends Exception{

    /**
     * Error message dto
     */
    @Getter
    private ErrorMessageDto errorMessageDto;

    /**
     * Mailkeets exception
     * @param e exception
     */
    public MailkeetsSendMailException(Exception e) {
        super(e);
    }

    /**
     * Mailkeets exception
     * @param errorMessageDto error dto
     */
    public MailkeetsSendMailException(ErrorMessageDto errorMessageDto) {
        super(errorMessageDto.getMessage());
        this.errorMessageDto = errorMessageDto;
    }

    /**
     * Mailkeets exception
     * @param message string
     */
    public MailkeetsSendMailException(String message) {
        super(message);
    }
}
