package org.example.trainschedule.exceptions;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;

public class ApiError {
    private final HttpStatus status;
    private final String message;
    private final List<String> errors;
    private final LocalDateTime timestamp;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}