package com.example.pathshala_plus_backend.entity;

import com.example.pathshala_plus_backend.enums.Difficulty;
import com.example.pathshala_plus_backend.enums.GenerationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "questions")
public class Question extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    Chapter chapter;

    @Column(columnDefinition = "TEXT")
    String questionText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    Language language;

    @Column(columnDefinition = "TEXT")
    String explanation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 10)
    Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    GenerationType generationType;

    @Column(length = 500)
    String imageUrl;

    @Column(nullable = false, columnDefinition = "boolean default false")
    Boolean isApproved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    Subject subject;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<QuestionOption> options = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passage_id")
    Passage passage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_session_id",nullable = false)
    ExamSession examSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id")
    Shift shift;

}
