package org.example.trainschedule.exceptions;

import java.util.List;

public class MyValidationException extends RuntimeException {
    private final List<String> errors;

    public MyValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return this.errors;
    }
}
