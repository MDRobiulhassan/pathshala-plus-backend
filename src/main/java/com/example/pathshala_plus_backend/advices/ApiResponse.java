package com.example.pathshala_plus_backend.advices;

import lombok.Data;

import java.time.Instant;

@Data
public class ApiResponse<T> {
    private Instant timestamp;
    private T data;
    private ApiError error;
    private boolean success;

    public ApiResponse() {
        this.timestamp = Instant.now();
    }

    public ApiResponse(T data) {
        this();
        this.data = data;
        this.success = true;
    }

    public ApiResponse(ApiError error) {
        this();
        this.error = error;
        this.success = false;
    }
}
