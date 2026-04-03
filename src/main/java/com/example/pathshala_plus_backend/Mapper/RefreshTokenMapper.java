package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.RefreshToken;
import com.example.pathshala_plus_backend.entity.User;
import lombok.experimental.UtilityClass;

import java.time.Instant;

@UtilityClass
public class RefreshTokenMapper {
    public static RefreshToken toRefreshToken(User user, String hashedToken, Instant expiresAt) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(hashedToken);
        refreshToken.setExpiresAt(expiresAt);
        refreshToken.setIsRevoked(false);
        return refreshToken;
    }
}
