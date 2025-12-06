package com.app.ed.emailservice.exceptions;

public class NonRetriableMessageException extends RuntimeException{

    public NonRetriableMessageException(String message){
        super(message);
    }
    public NonRetriableMessageException(Throwable throwable){
        super(throwable);
    }
}
