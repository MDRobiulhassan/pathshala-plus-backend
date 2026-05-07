package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.request.auth.SignupRequest;
import com.example.pathshala_plus_backend.entity.EducationLevel;
import com.example.pathshala_plus_backend.entity.Language;
import com.example.pathshala_plus_backend.entity.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.password.PasswordEncoder;

@UtilityClass
public class UserMapper {
    public static User fromSignupRequest(SignupRequest signupRequest,
                                   Language language,
                                   EducationLevel educationLevel,
                                   PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setName(signupRequest.name());
        user.setEmail(signupRequest.email());
        user.setPassword(passwordEncoder.encode(signupRequest.password()));
        user.setPreferredLanguage(language);
        user.setEducationLevel(educationLevel);
        user.setIsActive(true);
        return user;
    }

    public static User fromOauth2UserInfo(String name,String email,Language preferredLanguage,String avatarUrl) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPreferredLanguage(preferredLanguage);
        user.setAvatarUrl(avatarUrl);
        user.setIsActive(true);
        return user;
    }
}
