package com.example.usermanagement.api.model;

import com.example.usermanagement.api.validation.ValidUsername;
import com.example.usermanagement.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserAccountRequest(
    @ValidUsername
    String username,

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Enter valid email address.")
    String email,

    Gender gender,

    @Max(value = 100, message = "Age must be at most 100")
    @Min(value = 18, message = "Age must be at least 18")
    int age
) {
}
