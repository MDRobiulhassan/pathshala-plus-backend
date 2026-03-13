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
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_user_phone", columnNames = "phone")
        }
)
public class User extends AuditableEntity {

    @Column(nullable = false, length = 100)
    String name;

    @Column(nullable = false,length = 100)
    String email;

    @Column(length = 20)
    String phone;

    @Column(nullable = false)
    String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preferred_language_id", nullable = false)
    Language preferredLanguage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_level_id", nullable = false)
    EducationLevel educationLevel;

    @Column(nullable = false,columnDefinition = "boolean default true")
    Boolean isActive = true;

    Instant lastLoginAt;

    @Column(length = 500)
    String avatarUrl;
}
