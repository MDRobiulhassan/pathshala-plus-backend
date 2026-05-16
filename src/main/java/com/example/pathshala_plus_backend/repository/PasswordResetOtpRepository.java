package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.PasswordResetOtp;
import com.example.pathshala_plus_backend.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<@NonNull PasswordResetOtp, @NonNull Long> {
    List<PasswordResetOtp> findByUserAndIsUsedFalseAndIsDeletedFalse(User user);

    Optional<PasswordResetOtp> findByUserAndOtpAndIsUsedFalseAndIsDeletedFalse(User user, String otp);

    Optional<PasswordResetOtp> findByResetTokenAndIsUsedFalseAndIsDeletedFalse(String resetToken);
}
