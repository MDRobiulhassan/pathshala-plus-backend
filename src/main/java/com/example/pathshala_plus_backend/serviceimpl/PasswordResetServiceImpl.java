package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.Mapper.PasswordResetOtpMapper;
import com.example.pathshala_plus_backend.dto.request.passwordreset.ForgotPasswordRequest;
import com.example.pathshala_plus_backend.dto.request.passwordreset.ResetPasswordRequest;
import com.example.pathshala_plus_backend.dto.request.passwordreset.VerifyOtpRequest;
import com.example.pathshala_plus_backend.dto.response.passwordreset.VerifyOtpResponse;
import com.example.pathshala_plus_backend.entity.PasswordResetOtp;
import com.example.pathshala_plus_backend.entity.User;
import com.example.pathshala_plus_backend.event.PasswordResetOtpEvent;
import com.example.pathshala_plus_backend.exceptions.ResourceNotFoundException;
import com.example.pathshala_plus_backend.repository.PasswordResetOtpRepository;
import com.example.pathshala_plus_backend.repository.UserRepository;
import com.example.pathshala_plus_backend.service.PasswordResetService;
import com.example.pathshala_plus_backend.util.OtpGenerator;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.otp-expiration-minutes}")
    private Integer otpExpirationMinutes;

    @Value("${app.reset-token-expiration-minutes}")
    private Integer resetTokenExpirationMinutes;

    @Override
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        User user = userRepository.findByEmail(forgotPasswordRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + forgotPasswordRequest.email() + " not found"));
        invalidatePreviousOtpAndResetTokens(user);
        String otp = generateOtp(user);
        applicationEventPublisher.publishEvent(new PasswordResetOtpEvent(user.getEmail(), otp));
    }

    @Override
    public VerifyOtpResponse verifyOtpAndGetResetToken(VerifyOtpRequest otpRequest) {
        User user = userRepository.findByEmail(otpRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + otpRequest.email() + " not found"));
        PasswordResetOtp otp = passwordResetOtpRepository
                .findByUserAndOtpAndIsUsedFalseAndIsDeletedFalse(user, DigestUtils.sha256Hex(otpRequest.otp()))
                .orElseThrow(() -> new ResourceNotFoundException("Invalid OTP"));
        if (otp.getOtpExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("OTP has expired.");
        }
        return new VerifyOtpResponse(generateResetToken(otp));
    }


    @Override
    public void resetPassword(ResetPasswordRequest resetTokenRequest) {
        PasswordResetOtp resetToken = validateResetRequest(resetTokenRequest);

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(resetTokenRequest.newPassword()));
        userRepository.save(user);

        resetToken.setIsUsed(true);
        resetToken.softDelete(user.getId());
        passwordResetOtpRepository.save(resetToken);
    }

    private void invalidatePreviousOtpAndResetTokens(User user) {
        List<PasswordResetOtp> previousResetOtp = passwordResetOtpRepository
                .findByUserAndIsUsedFalseAndIsDeletedFalse(user);

        for (PasswordResetOtp resetOtp : previousResetOtp) {
            resetOtp.setIsUsed(true);
            resetOtp.softDelete(user.getId());
        }
        passwordResetOtpRepository.saveAll(previousResetOtp);
    }

    private String generateOtp(User user) {
        String otp = OtpGenerator.generate6DigitOtp();
        PasswordResetOtp passwordResetOtp = PasswordResetOtpMapper.toOtp(user, otp, Instant.now().plus(otpExpirationMinutes, ChronoUnit.MINUTES));
        passwordResetOtpRepository.save(passwordResetOtp);
        return otp;
    }

    private String generateResetToken(PasswordResetOtp existingOtp) {
        String resetToken = UUID.randomUUID().toString();
        existingOtp.setResetToken(DigestUtils.sha256Hex(resetToken));
        existingOtp.setResetTokenExpiresAt(Instant.now().plus(resetTokenExpirationMinutes, ChronoUnit.MINUTES));
        passwordResetOtpRepository.save(existingOtp);
        return resetToken;
    }

    private PasswordResetOtp validateResetRequest(ResetPasswordRequest resetPasswordRequest) {
        if (!resetPasswordRequest.newPassword().equals(resetPasswordRequest.confirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }

        PasswordResetOtp resetOtp = passwordResetOtpRepository
                .findByResetTokenAndIsUsedFalseAndIsDeletedFalse(DigestUtils.sha256Hex(resetPasswordRequest.resetToken()))
                .orElseThrow(() -> new RuntimeException("Invalid reset token."));

        if (resetOtp.getResetTokenExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Reset token is invalid or expired.");
        }

        return resetOtp;
    }
}
