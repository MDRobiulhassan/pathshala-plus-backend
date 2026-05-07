package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.entity.Role;
import com.example.pathshala_plus_backend.enums.SignupRole;

public interface RoleService {
    Role findRoleByName(SignupRole signupRole);
}
