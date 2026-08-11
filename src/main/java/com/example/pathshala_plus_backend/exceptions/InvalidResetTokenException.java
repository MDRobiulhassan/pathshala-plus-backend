package com.example.pathshala_plus_backend.exceptions;

public class InvalidResetTokenException extends BadRequestException {
    public InvalidResetTokenException(String message) {
        super(message);
    }
}
