package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.request.passwordreset.ForgotPasswordRequest;
import com.example.pathshala_plus_backend.dto.request.passwordreset.ResetPasswordRequest;
import com.example.pathshala_plus_backend.dto.request.passwordreset.VerifyOtpRequest;
import com.example.pathshala_plus_backend.dto.response.passwordreset.VerifyOtpResponse;

public interface PasswordResetService {
    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    VerifyOtpResponse verifyOtpAndGetResetToken(VerifyOtpRequest otpRequest);
    void resetPassword(ResetPasswordRequest resetTokenRequest);
}
