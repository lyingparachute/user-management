package com.example.usermanagement.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum Gender {
    MALE("male"),
    FEMALE("female");

    private final String value;

    @JsonCreator
    public static Gender fromString(final String string) {
        return Arrays.stream(values())
            .filter(type -> type.toString().equalsIgnoreCase(string))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("%s is improper value for Gender argument. Should be one of: %s",
                string, Arrays.stream(values()).toList())));
    }
}
