package com.client.demo.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class FatalException extends RuntimeException {

    @Getter
    private HttpStatus httpStatus;

    public FatalException(String message) {
        super(message);
    }

    public FatalException(HttpStatus status, String body) {
        super("[" + status + "] " + body);
        this.httpStatus = status;
    }
}
