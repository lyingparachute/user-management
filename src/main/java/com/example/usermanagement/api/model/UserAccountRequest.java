package com.example.usermanagement.api.model;

import com.example.usermanagement.api.validation.ValidUsername;
import com.example.usermanagement.model.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UserAccountRequest(
    @ValidUsername
    String username,

    Gender gender,

    @Max(value = 100, message = "Age must be at most 100")
    @Min(value = 18, message = "Age must be at least 18")
    int age
) {
}
