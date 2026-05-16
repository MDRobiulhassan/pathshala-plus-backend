package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.PasswordResetOtp;
import com.example.pathshala_plus_backend.entity.User;
import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.Instant;

@UtilityClass
public class PasswordResetOtpMapper {
    public static PasswordResetOtp toOtp(User user, String otp, Instant expiresAt){
        PasswordResetOtp passwordResetOtp = new PasswordResetOtp();
        passwordResetOtp.setUser(user);
        passwordResetOtp.setOtp(DigestUtils.sha256Hex(otp));
        passwordResetOtp.setOtpExpiresAt(expiresAt);
        return passwordResetOtp;
    }
}
