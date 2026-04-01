package com.example.pathshala_plus_backend.entity;

import com.example.pathshala_plus_backend.enums.OptionLabel;
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
@Table(name = "test_answers")
public class TestAnswer extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    TestAttempt testAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    Question question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 5)
    OptionLabel selectedOption;

    @Column(nullable = false)
    Boolean isCorrect;

    @Column(nullable = false)
    Float marksAwarded;

    @Column(nullable = false)
    Instant answeredAt;
}
