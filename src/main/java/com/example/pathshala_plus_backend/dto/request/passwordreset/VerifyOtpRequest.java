package com.example.pathshala_plus_backend.dto.request.passwordreset;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyOtpRequest(
        @NotBlank @Email String email,
        @NotBlank String otp
) {
    public VerifyOtpRequest {
        email = email == null ? null : email.trim();
    }
}
