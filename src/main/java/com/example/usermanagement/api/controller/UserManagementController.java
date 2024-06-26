package com.example.usermanagement.api.controller;

import com.example.usermanagement.api.model.CreateUserAccountRequest;
import com.example.usermanagement.api.model.UserAccountResponse;
import com.example.usermanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("user-details")
@RequiredArgsConstructor
public class UserManagementController {
    private final UserService userService;

    @GetMapping("{id}")
    ResponseEntity<UserAccountResponse> getUserDetails(@PathVariable final Long id) {
        return ResponseEntity.ok(userService.getUserAccount(id));
    }

    @PostMapping
    ResponseEntity<UserAccountResponse> createUserDetails(@RequestBody @Validated final CreateUserAccountRequest createUserAccountRequest,
                                                          final HttpServletRequest httpRequest) {

        final var userAccount = userService.createUserAccount(createUserAccountRequest, httpRequest);
        return ResponseEntity.created(URI.create("/user-details/" + userAccount.id()))
            .body(userAccount);
    }

    @PatchMapping("{id}")
    ResponseEntity<UserAccountResponse> updateUserDetails(@PathVariable final Long id,
                                                          @RequestBody @Validated final CreateUserAccountRequest createUserAccountRequest) {
        return ResponseEntity.ok(userService.updateUserAccount(id, createUserAccountRequest));
    }

    @DeleteMapping("{id}")
    ResponseEntity<Void> deleteUserDetails(@PathVariable final Long id) {
        userService.removeUserAccount(id);
        return ResponseEntity.noContent().build();
    }
}
