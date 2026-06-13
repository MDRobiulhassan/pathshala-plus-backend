package com.example.pathshala_plus_backend.dto.request.passwordreset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest (
        @NotBlank String resetToken,
        @NotBlank  @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$",
                message = "Password must contain uppercase, lowercase, number, and special character"
        ) String newPassword,
        @NotBlank (message = "Confirm password is required") String confirmPassword
) {
}
