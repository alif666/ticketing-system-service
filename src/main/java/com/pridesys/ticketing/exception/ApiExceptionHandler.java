package com.pridesys.ticketing.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class) @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Map<String, String> badCredentials(BadCredentialsException e) { return Map.of("error", "UNAUTHORIZED", "message", e.getMessage()); }
    @ExceptionHandler(MethodArgumentNotValidException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> validation(MethodArgumentNotValidException e) { return Map.of("error", "VALIDATION_ERROR", "message", "Request validation failed"); }
}
