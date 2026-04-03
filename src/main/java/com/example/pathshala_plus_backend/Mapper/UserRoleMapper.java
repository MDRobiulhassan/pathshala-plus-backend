package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.Role;
import com.example.pathshala_plus_backend.entity.User;
import com.example.pathshala_plus_backend.entity.UserRole;
import lombok.experimental.UtilityClass;

import java.time.Instant;

@UtilityClass
public class UserRoleMapper {
    public static UserRole userRole(User user, Role role){
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setAssignedAt(Instant.now());
        userRole.setAssignedBy(user);
        return userRole;
    }
}
