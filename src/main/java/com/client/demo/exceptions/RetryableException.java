package com.client.demo.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;


public class RetryableException extends RuntimeException {
    @Getter
    private final HttpStatus status;
    @Getter
    private final String body;

    public RetryableException(HttpStatus status, String body) {
        super("[" + status + "] " + body);
        this.status = status;
        this.body = body;
    }
}
