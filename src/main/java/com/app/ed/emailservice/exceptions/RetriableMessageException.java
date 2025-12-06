package com.app.ed.emailservice.exceptions;

public class RetriableMessageException extends RuntimeException{
    public RetriableMessageException(String message) {
        super(message);
    }
    public RetriableMessageException(Throwable throwable) {
        super(throwable);
    }
}
