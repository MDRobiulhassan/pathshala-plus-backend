package com.example.pathshala_plus_backend.entity;

import com.example.pathshala_plus_backend.enums.EducationCategory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "education_levels")
public class EducationLevel extends AuditableEntity {

    @Column(nullable = false, length = 100)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    EducationCategory educationCategory;

    @Column(columnDefinition = "TEXT")
    String description;
}
