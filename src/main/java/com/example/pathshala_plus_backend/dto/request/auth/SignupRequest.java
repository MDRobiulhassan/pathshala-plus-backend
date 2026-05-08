package com.example.pathshala_plus_backend.dto.request.auth;

import com.example.pathshala_plus_backend.enums.SignupRole;
import jakarta.validation.constraints.*;

public record SignupRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8,max = 50) String password,
        @NotBlank String confirmPassword,
        @NotBlank String preferredLanguageCode,
        @NotNull Long educationLevelId,
        @NotNull SignupRole role
) {
}
