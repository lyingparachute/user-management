package com.example.usermanagement.api.model;

import com.example.usermanagement.model.Gender;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserAccountResponse(
    Long id,
    String username,
    String email,
    Gender gender,
    int age,
    boolean active,
    Instant createdAt
) {
}
