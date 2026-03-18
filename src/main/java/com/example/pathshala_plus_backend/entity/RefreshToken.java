package com.example.pathshala_plus_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        name = "refresh_tokens",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_refresh_token",
                        columnNames = "token"
                )
        }
)
public class RefreshToken extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false)
    String token;

    @Column(nullable = false)
    Instant expiresAt;

    @Column(nullable = false)
    Boolean isRevoked = false;

    Instant revokedAt;

    Long revokedBy;
    String deviceInfo;

    @Column(length = 45)
    String ipAddress;
}
