package com.example.pathshala_plus_backend.advices;

import com.example.pathshala_plus_backend.enums.SignupRole;
import com.example.pathshala_plus_backend.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException exception) {
        ApiError apiError = ApiError.builder().status(HttpStatus.NOT_FOUND).message(exception.getMessage()).build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedException exception) {
        ApiError apiError = ApiError.builder().status(HttpStatus.UNAUTHORIZED).message(exception.getMessage()).build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbidden(ForbiddenException exception) {
        ApiError apiError = ApiError.builder().status(HttpStatus.FORBIDDEN).message(exception.getMessage()).build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResource(DuplicateResourceException exception) {
        ApiError apiError = ApiError.builder().status(HttpStatus.CONFLICT).message(exception.getMessage()).build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException exception) {
        ApiError apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST).message(exception.getMessage()).build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        ApiError apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST).message("Input Validation Failed").subErrors(errors).build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<?> handleInvalidEnum(HttpMessageConversionException exception) {
        String validRoles = String.join(", ",
                Arrays.stream(SignupRole.values()).map(Enum::name).toList());
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Invalid role value. Valid roles: " + validRoles)
                .build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidResetTokenException.class)
    public ResponseEntity<?> handleInvalidResetToken(InvalidResetTokenException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleInternalServerError(Exception exception) {
        log.error("Unhandled exception occurred: ", exception);
        ApiError apiError = ApiError.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message("An Unexpected Error Occurred").build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
