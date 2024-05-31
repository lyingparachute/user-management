package com.example.usermanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
public class UserDetails {
    @Id
    private UUID id;
    @Column(unique = true)
    private String username;
    private Gender gender;
    private int age;
    private Instant createdAt;
}
