package com.example.pathshala_plus_backend.event;

public record PasswordResetOtpEvent(
        String email, String otp
) {
}
