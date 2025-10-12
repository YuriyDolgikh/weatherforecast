package com.weatherforecast.exception;

import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

public class AlreadyExistException extends RuntimeJsonMappingException {
    public AlreadyExistException(String message) {
        super(message);
    }
}
