package com.example.usermanagement.api.controller;

import com.example.usermanagement.model.UserDetailsDto;
import com.example.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("users")
@RequiredArgsConstructor
public class UserManagementController {
    private final UserService userService;

    @GetMapping("{id}")
    ResponseEntity<UserDetailsDto> retrieveUser(@PathVariable @Validated final UUID id) {
        return ResponseEntity.ok(userService.getUserDetails(id));
    }
}
