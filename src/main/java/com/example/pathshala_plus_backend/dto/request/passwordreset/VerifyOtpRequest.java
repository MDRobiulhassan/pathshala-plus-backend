package com.example.pathshala_plus_backend.dto.request.passwordreset;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VerifyOtpRequest (
        @NotBlank @Email String email,
        @NotBlank String otp
) {
}
