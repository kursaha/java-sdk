package com.kursaha.mailkeets.exception;

public class MailkeetsSendMailException extends Exception{
    public MailkeetsSendMailException () {
        super("Failed to send mail");
    }
    public MailkeetsSendMailException(Exception e) {
        super(e);
    }
}
