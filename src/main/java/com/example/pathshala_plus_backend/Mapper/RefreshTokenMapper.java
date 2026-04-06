package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.RefreshToken;
import com.example.pathshala_plus_backend.entity.User;
import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.Instant;

@UtilityClass
public class RefreshTokenMapper {
    public static RefreshToken toRefreshToken(User user, String rawToken, Instant expiresAt) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(DigestUtils.sha256Hex(rawToken));
        refreshToken.setExpiresAt(expiresAt);
        refreshToken.setIsRevoked(false);
        return refreshToken;
    }
}
