package com.app.ed.emailservice.exceptions;

import com.app.ed.emailservice.model.ErrorResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalHandlerException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RetriableMessageException.class)
    public ResponseEntity<ErrorResponseMessage> retriableMessageExceptionHandler(RetriableMessageException retriableMessageException,
                                                                   WebRequest request) {
        ErrorResponseMessage messageBuilder =
                ErrorResponseMessage.builder()
                        .path(request.getDescription(false))
                        .message(retriableMessageException.getMessage())
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .localDateTime(LocalDateTime.now())
                        .build();
        return new ResponseEntity<>(messageBuilder, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
