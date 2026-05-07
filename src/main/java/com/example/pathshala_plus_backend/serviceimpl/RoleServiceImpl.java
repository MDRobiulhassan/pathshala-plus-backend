package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.entity.Role;
import com.example.pathshala_plus_backend.enums.RoleName;
import com.example.pathshala_plus_backend.enums.SignupRole;
import com.example.pathshala_plus_backend.exceptions.ResourceNotFoundException;
import com.example.pathshala_plus_backend.repository.RoleRepository;
import com.example.pathshala_plus_backend.repository.UserRoleRepository;
import com.example.pathshala_plus_backend.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;

    @Override
    public Role findRoleByName(SignupRole signupRole) {
        return roleRepository.findByName(RoleName.valueOf(signupRole.name()))
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + signupRole.name()));
    }
}
