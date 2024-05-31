package com.example.usermanagement.model;

import lombok.Builder;

import java.time.Instant;

@Builder
public record UserDetailsDto(
    String username,
    Gender gender,
    int age,
    Instant createdAt
) {
}
