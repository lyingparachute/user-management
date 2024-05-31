package com.example.usermanagement.service;

import com.example.usermanagement.api.model.UserAccountRequest;
import com.example.usermanagement.api.model.UserAccountResponse;
import com.example.usermanagement.exception.UsernameExistsException;
import com.example.usermanagement.model.UserAccount;
import com.example.usermanagement.model.UserAccountRepository;
import com.example.usermanagement.model.UserDetails;
import com.example.usermanagement.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserAccountRepository userAccountRepository;

    public UserAccountResponse getUserAccount(final Long userId) {
        final var userAccount = findById(userId);
        return UserMapper.toResponse(userAccount);
    }

    public UserAccountResponse createUserAccount(final UserAccountRequest userAccountRequest) {
        validateUsername(userAccountRequest.username());
        final var saved = userAccountRepository.save(
            UserAccount.builder()
                .userDetails(UserDetails.builder()
                    .username(userAccountRequest.username())
                    .gender(userAccountRequest.gender())
                    .age(userAccountRequest.age())
                    .build()
                )
                .createdAt(Instant.now())
                .build()
        );
        return UserMapper.toResponse(saved);
    }

    public UserAccountResponse updateUserAccount(final Long userId, final UserAccountRequest userAccountRequest) {
        final var userAccount = findById(userId);
        userAccount.setUserDetails(
            UserDetails.builder()
                .username(userAccountRequest.username())
                .gender(userAccountRequest.gender())
                .age(userAccountRequest.age())
                .build());
        final var savedUser = userAccountRepository.save(userAccount);
        return UserMapper.toResponse(savedUser);
    }

    private UserAccount findById(final Long userId) {
        return userAccountRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(String.format("User with ID: %s not found", userId)));
    }

    private void validateUsername(final String username) {
        if (userAccountRepository.findByUsername(username).isPresent())
            throw new UsernameExistsException("Username already exists: " + username);
    }
}
