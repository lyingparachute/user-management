package com.example.usermanagement.api.model;

import com.example.usermanagement.model.Gender;

public record UserAccountRequest(
    String username,
    Gender gender,
    int age
) {
}
