package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.Role;
import com.example.pathshala_plus_backend.entity.User;
import com.example.pathshala_plus_backend.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "assignedBy", source = "user")
    @Mapping(target = "assignedAt", expression = "java(java.time.Instant.now())")
    UserRole toUserRole(User user, Role role);
}