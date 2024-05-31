package com.example.usermanagement.test.util;

import com.example.usermanagement.api.model.UserAccountRequest;
import com.example.usermanagement.model.Gender;
import com.example.usermanagement.model.UserAccount;
import com.example.usermanagement.model.UserDetails;

import java.time.Instant;

public class InitData {
    public UserAccount createUser() {
        return UserAccount.builder()
            .id(1L)
            .userDetails(
                UserDetails.builder()
                    .username("johndoe")
                    .email("john.doe@gmail.com")
                    .gender(Gender.MALE)
                    .age(22)
                    .build()
            )
            .activated(true)
            .createdAt(Instant.now())
            .build();
    }

    public UserAccount createUnverifiedUser() {
        return UserAccount.builder()
            .id(1L)
            .userDetails(
                UserDetails.builder()
                    .username("johndoe")
                    .email("john.doe@gmail.com")
                    .gender(Gender.MALE)
                    .age(22)
                    .build()
            )
            .createdAt(Instant.now())
            .activated(false)
            .build();
    }

    public UserAccountRequest createRegisterRequest() {
        return UserAccountRequest.builder()
            .username("johndoe")
            .email("john.doe@gmail.com")
            .gender(Gender.MALE)
            .age(22)
            .build();
    }
}
