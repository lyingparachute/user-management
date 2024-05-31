package com.example.usermanagement.exception;

public final class UsernameExistsException extends RuntimeException {

    public UsernameExistsException(final String message) {
        super(message);
    }
}
