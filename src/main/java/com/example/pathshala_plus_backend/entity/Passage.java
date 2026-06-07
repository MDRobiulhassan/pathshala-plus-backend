package com.example.pathshala_plus_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "passages")
public class Passage extends AuditableEntity{
    @Column(columnDefinition = "TEXT")
    String passageText;

    @Column(length = 500)
    String imageUrl;

    @OneToMany(mappedBy = "passage", fetch = FetchType.LAZY)
    List<Question> questions = new ArrayList<>();
}
