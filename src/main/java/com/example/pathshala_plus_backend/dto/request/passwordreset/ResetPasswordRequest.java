package com.example.pathshala_plus_backend.dto.request.passwordreset;

public record ResetPasswordRequest (
        String resetToken,
        String newPassword,
        String confirmPassword
) {
}
