package com.example.pathshala_plus_backend.dto.request.auth;

import com.example.pathshala_plus_backend.enums.SignupRole;
import jakarta.validation.constraints.*;

public record SignupRequest(
        @NotBlank @Size(min =3,max=100) @Pattern(regexp = "^[A-Za-z ]+$") String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8,max = 50) @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$",
                message = "Password must contain uppercase, lowercase, number, and special character"
        ) String password,
        @NotBlank String confirmPassword,
        @NotBlank String preferredLanguageCode,
        @NotNull Long educationLevelId,
        @NotNull SignupRole role
) {
}
