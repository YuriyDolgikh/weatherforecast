package com.weatherforecast.controller;

import com.weatherforecast.exception.AlreadyExistException;
import com.weatherforecast.exception.BadRequestException;
import com.weatherforecast.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handlerNullPointerException(NullPointerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handlerNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<String> handlerAlreadyExistException(AlreadyExistException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerConstraintViolationException(ConstraintViolationException e) {
        StringBuilder responseMessage = new StringBuilder();

        e.getConstraintViolations().forEach(
                constraintViolation -> {
                    String currentField = constraintViolation.getPropertyPath().toString();
                    String currentMessage = constraintViolation.getMessage();
                    responseMessage.append("Field : " + currentField + " : " + currentMessage);
                    responseMessage.append("\n");
                }
        );
        return new ResponseEntity<>(responseMessage.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Validation failed");
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        body.put("errors", errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getRootCause() != null ? ex.getRootCause() : ex.getMostSpecificCause();
        // fallback
        String msg = "Invalid request body";

        // 1) Json: неверный тип/формат поля (enum и LocalDate)
        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
            com.fasterxml.jackson.databind.exc.InvalidFormatException ife =
                    (com.fasterxml.jackson.databind.exc.InvalidFormatException) cause;

            Class<?> target = ife.getTargetType();
            String field = ife.getPath().isEmpty() ? "value" : ife.getPath().get(0).getFieldName();

            // LocalDate
            if (java.time.LocalDate.class.equals(target)) {
                msg = "Invalid value for '" + field + "'. Use date format yyyy-MM-dd (e.g. 2025-10-08)";
                return bad(msg);
            }

            // Enum
            if (target.isEnum()) {
                String allowed = java.util.Arrays.stream(target.getEnumConstants())
                        .map(Object::toString)
                        .collect(java.util.stream.Collectors.joining(", "));
                msg = "Invalid value for '" + field + "'. Allowed: " + allowed;
                return bad(msg);
            }
        }

        // 2) Когда первопричина — DateTimeParseException
        if (cause instanceof java.time.format.DateTimeParseException) {
            msg = "Invalid date format. Use yyyy-MM-dd (e.g. 2025-10-08)";
            return bad(msg);
        }

        // дефолт
        return bad(msg);
    }

    private ResponseEntity<Map<String, Object>> bad(String message) {
        return ResponseEntity.badRequest().body(java.util.Map.of("message", message));
    }
    //deadline
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String,Object>> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }


}
