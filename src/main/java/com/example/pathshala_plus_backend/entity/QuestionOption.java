package com.example.pathshala_plus_backend.entity;

import com.example.pathshala_plus_backend.enums.OptionLabel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "question_options")
public class QuestionOption extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    Question question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    OptionLabel optionLabel;

    @Column(columnDefinition = "TEXT")
    String optionText;

    @Column(nullable = false, columnDefinition = "boolean default false")
    Boolean isCorrect = false;

    @Column(length = 500)
    String imageUrl;
}
