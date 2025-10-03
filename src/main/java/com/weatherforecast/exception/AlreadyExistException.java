package com.weatherforecast.exception;

import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

public class AlreadyExistException extends RuntimeJsonMappingException {
    public AlreadyExistException(String message) {
        super(message);
    }
}
