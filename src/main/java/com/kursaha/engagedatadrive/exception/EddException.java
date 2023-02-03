package com.kursaha.engagedatadrive.exception;

import com.kursaha.ErrorMessageDto;
import lombok.Getter;

/**
 * Mailkeets exception
 */
public class EddException extends Exception{

    /**
     * Error message dto
     */
    @Getter
    private ErrorMessageDto errorMessageDto;

    /**
     * Mailkeets exception
     * @param e exception
     */
    public EddException(Exception e) {
        super(e);
    }

    /**
     * Mailkeets exception
     * @param errorMessageDto error dto
     */
    public EddException(ErrorMessageDto errorMessageDto) {
        super(errorMessageDto.getMessage());
        this.errorMessageDto = errorMessageDto;
    }

    /**
     * Mailkeets exception
     * @param message string
     */
    public EddException(String message) {
        super(message);
    }
}
