package com.example.pathshala_plus_backend.entity;

import com.example.pathshala_plus_backend.entity.enums.PermissionModule;
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
        name = "permissions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_permission_name", columnNames = "name")
        }
)
public class Permission extends AuditableEntity {

    @Column(nullable = false, length = 100)
    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    PermissionModule module;
}
