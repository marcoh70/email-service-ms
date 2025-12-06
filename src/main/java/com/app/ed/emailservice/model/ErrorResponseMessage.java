package com.app.ed.emailservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponseMessage {

    private String path;
    private HttpStatus httpStatus;
    private String message;
    private LocalDateTime localDateTime;
}
