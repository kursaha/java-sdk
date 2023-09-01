package com.kursaha.common;

import lombok.Getter;

public class MailkeetsException extends Exception{
    @Getter
    private final ErrorMessageDto errorMessage;
    public MailkeetsException(ErrorMessageDto errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }

    public MailkeetsException(String message) {
        super(message);
        this.errorMessage = null;
    }

    @Override
    public String toString() {
        if (errorMessage!= null) {
            return errorMessage.toString();
        }
        return super.toString();
    }
}
