package com.example.usermanagement.util;

import com.example.usermanagement.api.model.UserAccountResponse;
import com.example.usermanagement.model.UserAccount;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class UserMapper {
    public static UserAccountResponse toResponse(final UserAccount user) {
        return UserAccountResponse.builder()
            .id(user.getId())
            .username(user.getUserDetails().username())
            .email(user.getUserDetails().email())
            .gender(user.getUserDetails().gender())
            .age(user.getUserDetails().age())
            .createdAt(user.getCreatedAt())
            .build();
    }
}
