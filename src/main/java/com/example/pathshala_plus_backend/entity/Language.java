package com.example.pathshala_plus_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        name = "languages",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_language_code", columnNames = "code")
        })
public class Language extends AuditableEntity {

    @Column(nullable = false, length = 10)
    String code;

    @Column(nullable = false, length = 50)
    String name;

    @Column(nullable = false,columnDefinition = "boolean default true")
    Boolean isActive = true;

    @Column(nullable = false,columnDefinition = "boolean default false")
    Boolean isDefault = false;

}
