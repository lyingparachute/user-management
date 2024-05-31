package com.example.usermanagement.service;

import com.example.usermanagement.model.UserDetailsDto;
import com.example.usermanagement.model.UserDetailsRepository;
import com.example.usermanagement.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDetailsRepository userDetailsRepository;

    public UserDetailsDto getUserDetails(final UUID userId) {
        final var userDetails = Optional.of(userDetailsRepository.findById(userId))
            .get().orElseThrow(() -> new IllegalArgumentException(String.format("User with ID: %s not found", userId)));
        return UserMapper.toDto(userDetails);
    }


}
