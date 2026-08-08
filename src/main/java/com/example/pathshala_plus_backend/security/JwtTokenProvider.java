package com.example.pathshala_plus_backend.security;

import com.example.pathshala_plus_backend.entity.RefreshToken;
import com.example.pathshala_plus_backend.entity.User;
import com.example.pathshala_plus_backend.exceptions.ResourceNotFoundException;
import com.example.pathshala_plus_backend.exceptions.UnauthorizedException;
import com.example.pathshala_plus_backend.repository.RefreshTokenRepository;
import com.example.pathshala_plus_backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtTokenProvider {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    private SecretKey signingKey;
    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${jwt.access-token-expiration-minutes}")
    private Integer accessTokenExpirationMinutes;

    @Value("${jwt.refresh-token-expiration-days}")
    private Integer refreshTokenExpirationDays;

    public JwtTokenProvider(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Authentication authentication) {
        return getToken(authentication, Duration.ofMinutes(accessTokenExpirationMinutes).toMillis(), "ACCESS");
    }

    public String generateRefreshToken(Authentication authentication) {
        invalidateRefreshTokens(resolveUser(authentication));
        return getToken(authentication, Duration.ofDays(refreshTokenExpirationDays).toMillis(), "REFRESH");
    }

    private String getToken(Authentication authentication, Long expirationMs, String tokenType) {
        User user = resolveUser(authentication);
        String username = user.getEmail();
        Long userId = user.getId();

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("type", tokenType)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }

    public boolean verifyAccessToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getExpiration().after(new Date())
                    && "ACCESS".equals(claims.get("type", String.class));
        } catch (Exception e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean verifyRefreshToken(String rawToken, User user) {
        if (!isRefreshToken(rawToken)) {
            return false;
        }

        String hashedToken = DigestUtils.sha256Hex(rawToken);

        RefreshToken token = refreshTokenRepository
                .findByUserAndIsDeletedFalseAndIsRevokedFalse(user).orElseThrow(() -> new ResourceNotFoundException("Refresh token not found for user"));
        return token.getToken().equals(hashedToken)
                && token.getExpiresAt().isAfter(Instant.now());
    }

    private boolean isRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return "REFRESH".equals(claims.get("type", String.class));
        } catch (Exception e) {
            log.warn("Failed to verify refresh token type: {}", e.getMessage());
            return false;
        }
    }

    public void invalidateRefreshTokens(User user) {
        List<RefreshToken> previousTokens = refreshTokenRepository.findByUserAndIsDeletedFalse(user);

        for (RefreshToken refreshToken : previousTokens) {
            refreshToken.setIsRevoked(true);
            refreshToken.setRevokedAt(Instant.now());
            refreshToken.softDelete(user.getId());
        }

        refreshTokenRepository.saveAll(previousTokens);
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userPrincipal)) {
            return null;
        }

        return userPrincipal.getId();
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            log.warn("Failed to extract username from token: {}", e.getMessage());
            return null;
        }
    }

    private User resolveUser(Authentication authentication) {
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } else if (authentication.getPrincipal() instanceof DefaultOAuth2User oAuth2User) {
            String email = oAuth2User.getAttribute("email");
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } else {
            throw new UnauthorizedException("Unknown authentication principal type");
        }
    }
}
