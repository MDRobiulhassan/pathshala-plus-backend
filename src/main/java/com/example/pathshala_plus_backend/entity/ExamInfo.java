package com.example.pathshala_plus_backend.entity;

import com.example.pathshala_plus_backend.enums.ExamType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "exam_info")
public class ExamInfo extends AuditableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_session_id", nullable = false)
    ExamSession examSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id")
    Shift shift;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    ExamType examType;

    @Column(nullable = false)
    Integer totalMarks;

    @Column(nullable = false)
    Integer durationInMinutes;

    @Column(nullable = false)
    Integer totalQuestions;

    @OneToOne(mappedBy = "examInfo")
    private Test test;
}
