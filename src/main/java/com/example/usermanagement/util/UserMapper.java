package com.example.usermanagement.util;

import com.example.usermanagement.model.UserDetails;
import com.example.usermanagement.model.UserDetailsDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class UserMapper {
    public static UserDetailsDto toDto(final UserDetails user) {
        return UserDetailsDto.builder()
            .username(user.getUsername())
            .gender(user.getGender())
            .age(user.getAge())
            .createdAt(user.getCreatedAt())
            .build();
    }
}
