package com.dev.libraryManagementSystem.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
public class libraryApiException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public libraryApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
