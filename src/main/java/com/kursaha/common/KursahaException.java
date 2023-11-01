package com.kursaha.common;

import lombok.Getter;

/**
 * Custom exception for Mailkeets
 */
public class KursahaException extends Exception{
    /**
     * use for parsing error message from server
     */
    @Getter
    private final ErrorMessageDto errorMessage;

    /**
     * constructor of MailkeetsException
     * @param errorMessage for parsing error message from server
     */
    public KursahaException(ErrorMessageDto errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        if (errorMessage!= null) {
            return errorMessage.toString();
        }
        return super.toString();
    }
}
