package com.pridesys.ticketing.exception;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    Map<String, String> forbidden(AccessDeniedException e) {
        return Map.of("error", "FORBIDDEN", "message", e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    Map<String, String> conflict(DataIntegrityViolationException e) {
        return Map.of("error", "CONFLICT", "message", "Resource violates a uniqueness or integrity constraint");
    }

    @ExceptionHandler(ResourceConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    Map<String, String> resourceConflict(ResourceConflictException e) {
        return Map.of("error", "CONFLICT", "message", e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> notFound(NoSuchElementException e) {
        return Map.of("error", "NOT_FOUND", "message", "Resource not found");
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Map<String, String> badCredentials(BadCredentialsException e) {
        return Map.of("error", "UNAUTHORIZED", "message", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> validation(MethodArgumentNotValidException e) {
        return Map.of("error", "VALIDATION_ERROR", "message", "Request validation failed");
    }
}
