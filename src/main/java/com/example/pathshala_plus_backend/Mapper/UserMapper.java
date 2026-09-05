package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.request.auth.SignupRequest;
import com.example.pathshala_plus_backend.entity.EducationLevel;
import com.example.pathshala_plus_backend.entity.Language;
import com.example.pathshala_plus_backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
}