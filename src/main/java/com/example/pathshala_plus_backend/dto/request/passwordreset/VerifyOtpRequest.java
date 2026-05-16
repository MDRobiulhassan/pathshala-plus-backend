package com.example.pathshala_plus_backend.dto.request.passwordreset;

public record VerifyOtpRequest (
        String email,
        String otp
) {
}
