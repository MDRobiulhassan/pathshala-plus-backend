package com.example.pathshala_plus_backend.entity;

import com.example.pathshala_plus_backend.entity.enums.TestTypeName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        name = "test_types",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_test_type_name", columnNames = "testTypeName")
        }
)
public class TestType extends AuditableEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    TestTypeName testTypeName;

    @Column(columnDefinition = "TEXT")
    String description;
}
