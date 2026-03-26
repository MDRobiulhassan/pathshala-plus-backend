package com.example.pathshala_plus_backend.entity;

import com.example.pathshala_plus_backend.entity.enums.Difficulty;
import com.example.pathshala_plus_backend.entity.enums.GenerationType;
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

    @Column(nullable = false, columnDefinition = "TEXT")
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

    @Column(length = 100,nullable = false)
    String source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    Subject subject;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<QuestionOption> options = new ArrayList<>();
}
