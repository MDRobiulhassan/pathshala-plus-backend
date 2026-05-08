package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.User;
import com.example.pathshala_plus_backend.entity.UserOauthAccount;
import com.example.pathshala_plus_backend.enums.Provider;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OauthUserMapper {
    public static UserOauthAccount toUserOauthAccount(User user, Provider provider, String providerId) {
        UserOauthAccount userOauthAccount = new UserOauthAccount();
        userOauthAccount.setUser(user);
        userOauthAccount.setProvider(provider);
        userOauthAccount.setProviderUserId(providerId);
        return userOauthAccount;
    }
}
