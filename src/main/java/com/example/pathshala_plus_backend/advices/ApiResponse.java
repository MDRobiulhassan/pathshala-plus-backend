package com.example.pathshala_plus_backend.advices;

import lombok.Data;

import java.time.Instant;

@Data
public class ApiResponse<T> {
    private Instant timestamps;
    private T data;
    private ApiError error;

    public ApiResponse() {
        this.timestamps = Instant.now();
    }

    public ApiResponse(T data) {
        this();
        this.data = data;
    }

    public ApiResponse(ApiError error) {
        this();
        this.error = error;
    }
}
