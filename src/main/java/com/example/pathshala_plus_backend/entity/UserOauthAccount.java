package com.example.pathshala_plus_backend.entity;

import com.example.pathshala_plus_backend.enums.Provider;
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
        name = "user_oauth_accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_provider",
                        columnNames = {"provider", "provider_user_id"}
                )
        }
)
public class UserOauthAccount extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    Provider provider;

    @Column(nullable = false, length = 100)
    String providerUserId;

    @Column(length = 100)
    String providerEmail;

    @Column(columnDefinition = "TEXT")
    String accessToken;

    Instant tokenExpiresAt;
}
