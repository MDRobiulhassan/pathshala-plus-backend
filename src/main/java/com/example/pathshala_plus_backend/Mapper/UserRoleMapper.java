package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.Role;
import com.example.pathshala_plus_backend.entity.User;
import com.example.pathshala_plus_backend.entity.UserRole;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "assignedBy", source = "user")
    @Mapping(target = "assignedAt", ignore = true)
    UserRole toUserRole(User user, Role role);

    @AfterMapping
    default void setAssignedAt(@MappingTarget UserRole userRole) {
        userRole.setAssignedAt(Instant.now());
    }
}