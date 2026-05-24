package com.example.pathshala_plus_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "institutes")
public class Institute extends AuditableEntity {

    @Column(nullable = false, length = 150)
    String name;

    @Column(length = 50)
    String description;

    @Column(length = 50)
    String shortName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_level_id")
    EducationLevel educationLevel;
}