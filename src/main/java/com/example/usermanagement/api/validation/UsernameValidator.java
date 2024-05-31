package com.example.usermanagement.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    @Override
    public void initialize(final ValidUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String usernameField,
                           final ConstraintValidatorContext context) {

        return usernameField != null && usernameField.matches("[a-zA-Z0-9]+")
            && (usernameField.length() > 4) && (usernameField.length() < 16);
    }
}
