package com.example.pathshala_plus_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "tests")
public class Test extends AuditableEntity {

    @Column(nullable = false, length = 100)
    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_type_id", nullable = false)
    TestType testType;

    @Column
    Integer durationMinutes;

    @Column(nullable = false)
    Integer totalQuestions;

    @Column(nullable = false)
    Float marksPerQuestion;

    @Column(nullable = false,columnDefinition = "float default 0.0")
    Float negativeMarksPerQuestion=0.0f;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_level_id", nullable = false)
    EducationLevel educationLevel;

    @Column(nullable = false, columnDefinition = "boolean default false")
    Boolean isPublic=false;

    @Column
    Float passMarks;
}
