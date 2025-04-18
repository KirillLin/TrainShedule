package org.example.trainschedule.exceptions;

import java.util.List;
import lombok.Getter;

@Getter
public class MyValidationException extends RuntimeException {
    private final List<String> errors;

    public MyValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }
}
