package com.example.pathshala_plus_backend.dto.request.passwordreset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest (
        @NotBlank String resetToken,
        @NotBlank  @Size(min = 8, max = 50)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$"
        ) String newPassword,
        @NotBlank String confirmPassword
) {
}
