package com.example.pathshala_plus_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private SecretKey signingKey;

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${jwt.access-token-expiration-minutes}")
    private Integer accessTokenExpirationMinutes;

    @Value("${jwt.refresh-token-expiration-days}")
    private Integer refreshTokenExpirationDays;

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Authentication authentication) {
        return getToken(authentication, Duration.ofMinutes(accessTokenExpirationMinutes).toMillis());
    }

    public String generateRefreshToken(Authentication authentication) {
        return getToken(authentication, Duration.ofDays(refreshTokenExpirationDays).toMillis());
    }

    private String getToken(Authentication authentication, Long expirationMs) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        assert user != null;

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }

    public boolean verifyToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Long userId = ((Integer) claims.get("userId")).longValue();
        String username = claims.getSubject();
        boolean notExpired = claims.getExpiration().after(new Date());
        return username.equals(claims.getSubject()) && notExpired;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userPrincipal)) {
            return null;
        }

        return userPrincipal.getId();
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser().verifyWith(signingKey).build()
                .parseSignedClaims(token).getPayload();

        return claims.getSubject();
    }

}
