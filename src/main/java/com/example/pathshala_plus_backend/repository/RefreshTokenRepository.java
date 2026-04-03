package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.RefreshToken;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<@NonNull RefreshToken,@NonNull Long> {
    Optional<RefreshToken> findByToken(String token);
}
