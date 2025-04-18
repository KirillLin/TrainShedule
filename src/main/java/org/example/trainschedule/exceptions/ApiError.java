package org.example.trainschedule.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Schema(hidden = true)
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

}