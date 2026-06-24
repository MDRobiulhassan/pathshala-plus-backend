package com.example.pathshala_plus_backend.controller;

import com.example.pathshala_plus_backend.dto.request.auth.LoginRequest;
import com.example.pathshala_plus_backend.dto.request.auth.SignupRequest;
import com.example.pathshala_plus_backend.dto.request.passwordreset.ForgotPasswordRequest;
import com.example.pathshala_plus_backend.dto.request.passwordreset.ResetPasswordRequest;
import com.example.pathshala_plus_backend.dto.request.passwordreset.VerifyOtpRequest;
import com.example.pathshala_plus_backend.service.AuthService;
import com.example.pathshala_plus_backend.service.PasswordResetService;
import com.example.pathshala_plus_backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/auth")
public class AuthController {

    AuthService authService;
    UserService userService;
    PasswordResetService passwordResetService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(signupRequest, response));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(loginRequest, response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        authService.refreshAccessToken(request, response);
        return ResponseEntity.ok().build();
    }

    // disable in production
    @PostMapping("/oauth2/google")
    public ResponseEntity<?> socialLogin() {
        return ResponseEntity.ok("/api/v1/auth/oauth2/google");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserProfile() {
        return ResponseEntity.ok(userService.getUserProfile());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        passwordResetService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {
        return ResponseEntity.ok(passwordResetService.verifyOtpAndGetResetToken(verifyOtpRequest));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        passwordResetService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok().build();
    }
}
