package com.example.pathshala_plus_backend.entity;

import com.example.pathshala_plus_backend.enums.Shift;
import com.example.pathshala_plus_backend.enums.Unit;
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

    @Enumerated(EnumType.STRING)
    @Column(length = 5)
    Unit unit;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    Shift shift;

    @Column(length = 50)
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_level_id")
    EducationLevel educationLevel;
}