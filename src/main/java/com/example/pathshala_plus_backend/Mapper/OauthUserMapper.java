package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.User;
import com.example.pathshala_plus_backend.entity.UserOauthAccount;
import com.example.pathshala_plus_backend.enums.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OauthUserMapper {

    @Mapping(target = "providerUserId", source = "providerId")
    UserOauthAccount toUserOauthAccount(User user, Provider provider, String providerId);
}