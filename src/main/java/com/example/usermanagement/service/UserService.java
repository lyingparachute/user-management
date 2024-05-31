package com.example.usermanagement.service;

import com.example.usermanagement.api.model.UserAccountRequest;
import com.example.usermanagement.api.model.UserAccountResponse;
import com.example.usermanagement.exception.UsernameExistsException;
import com.example.usermanagement.model.UserAccount;
import com.example.usermanagement.model.UserAccountRepository;
import com.example.usermanagement.model.UserDetails;
import com.example.usermanagement.util.UserMapper;
import com.example.usermanagement.verification.VerificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserAccountRepository userAccountRepository;
    private final VerificationService verificationService;

    public UserAccountResponse getUserAccount(final Long userId) {
        final var userAccount = findById(userId);
        return UserMapper.toResponse(userAccount);
    }

    public UserAccountResponse createUserAccount(final UserAccountRequest request,
                                                 final HttpServletRequest httpServletRequest) {
        validateUsername(request.username());
        final var saved = userAccountRepository.save(
            UserAccount.builder()
                .userDetails(UserDetails.builder()
                    .username(request.username())
                    .email(request.email())
                    .gender(request.gender())
                    .age(request.age())
                    .build()
                )
                .createdAt(Instant.now())
                .build()
        );
        verificationService.sendAccountVerificationEmail(httpServletRequest, saved);
        return UserMapper.toResponse(saved);
    }

    public UserAccountResponse updateUserAccount(final Long userId, final UserAccountRequest request) {
        final var userAccount = findById(userId);
        userAccount.setUserDetails(
            UserDetails.builder()
                .username(request.username())
                .email(request.email())
                .gender(request.gender())
                .age(request.age())
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

    public void removeUserAccount(final Long id) {
        userAccountRepository.deleteById(id);
    }
}
