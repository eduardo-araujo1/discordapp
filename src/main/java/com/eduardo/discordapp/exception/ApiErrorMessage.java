package com.eduardo.discordapp.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public record ApiErrorMessage(
        int status,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Map<String, String> errors
) {

    public ApiErrorMessage(HttpStatus status, String message, BindingResult result) {
        this(status.value(), message, createErrors(result));
    }

    public ApiErrorMessage(HttpStatus status, String message) {
        this(status.value(), message, null);
    }

    private static Map<String, String> createErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errors;
    }
}

