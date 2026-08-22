package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.RefreshToken;
import com.example.pathshala_plus_backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {

    @Mapping(target = "token", expression = "java(org.apache.commons.codec.digest.DigestUtils.sha256Hex(rawToken))")
    @Mapping(target = "expiresAt", source = "expiresAt")
    @Mapping(target = "isRevoked", constant = "false")
    RefreshToken toRefreshToken(User user, String rawToken, Instant expiresAt);
}