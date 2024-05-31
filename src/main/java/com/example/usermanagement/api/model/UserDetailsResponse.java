package com.example.usermanagement.api.model;

import com.example.usermanagement.model.Gender;
import lombok.Builder;

import java.time.Instant;

public record UserDetailsResponse(
    String username,
    Gender gender,
    int age,
    Instant createdAt
) {
}