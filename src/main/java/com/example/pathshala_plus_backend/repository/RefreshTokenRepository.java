package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.RefreshToken;
import com.example.pathshala_plus_backend.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<@NonNull RefreshToken, @NonNull Long> {
    Optional<RefreshToken> findByUserAndIsDeletedFalseAndIsRevokedFalse(User user);
    List<RefreshToken> findByUserIsDeletedFalse(User user);
}
