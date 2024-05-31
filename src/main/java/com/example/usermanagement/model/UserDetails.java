package com.example.usermanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;

@Embeddable
@Builder
public record UserDetails(
    @Column(unique = true)
    String username,
    String email,
    Gender gender,
    int age
) {
}
