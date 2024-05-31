package com.example.usermanagement.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    @GetMapping("account/verify")
    ResponseEntity<String> verifyAccount(@RequestParam("token") final String token) {
        try {
            verificationService.validateVerificationToken(token);
            verificationService.verifyAccount(token);
            return ResponseEntity.ok("Account successfully verified");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
