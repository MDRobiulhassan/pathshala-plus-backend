package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.PasswordResetOtp;
import com.example.pathshala_plus_backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface PasswordResetOtpMapper {

    @Mapping(target = "otp", expression = "java(org.apache.commons.codec.digest.DigestUtils.sha256Hex(otp))")
    @Mapping(target = "otpExpiresAt", source = "expiresAt")
    PasswordResetOtp toOtp(User user, String otp, Instant expiresAt);
}