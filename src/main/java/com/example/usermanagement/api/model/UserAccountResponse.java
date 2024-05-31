package com.example.usermanagement.api.model;

import com.example.usermanagement.model.Gender;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserAccountResponse(
    Long id,
    String username,
    Gender gender,
    int age,
    Instant createdAt
) {
}
