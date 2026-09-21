package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.request.auth.SignupRequest;
import com.example.pathshala_plus_backend.dto.response.user.UserProfileResponse;
import com.example.pathshala_plus_backend.entity.EducationLevel;
import com.example.pathshala_plus_backend.entity.Language;
import com.example.pathshala_plus_backend.entity.User;
import com.example.pathshala_plus_backend.enums.Provider;
import com.example.pathshala_plus_backend.enums.RoleName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "name", source = "signupRequest.name")
    @Mapping(target = "email", source = "signupRequest.email")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "preferredLanguage", source = "language")
    @Mapping(target = "educationLevel", source = "educationLevel")
    @Mapping(target = "isActive", constant = "true")
    User fromSignupRequest(SignupRequest signupRequest, Language language, EducationLevel educationLevel);

    @Mapping(target = "isActive", constant = "true")
    User fromOauth2UserInfo(String name, String email, Language preferredLanguage, String avatarUrl);

    @Mapping(target = "preferredLanguage", source = "user.preferredLanguage.name")
    @Mapping(target = "educationLevel", source = "user.educationLevel.name")
    @Mapping(target = "department", source = "user.educationLevel.department")
    @Mapping(target = "providers", source = "providers")
    @Mapping(target = "roles", source = "roles")
    UserProfileResponse toUserProfileResponse(User user, List<Provider> providers, List<RoleName> roles);
}