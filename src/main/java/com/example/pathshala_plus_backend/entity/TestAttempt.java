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
@Table(name = "test_attempts")
public class TestAttempt extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    Test test;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

//    Batch batch; --> V2
//    User teacher; --> v2

    @Column(nullable = false)
    Instant startedAt;

    Instant finishedAt;
    Float score;

    @Column(nullable = false, columnDefinition = "boolean default false")
    Boolean completed = false;
}
