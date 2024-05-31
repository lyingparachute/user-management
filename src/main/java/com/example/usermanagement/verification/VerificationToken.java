package com.example.usermanagement.verification;

import com.example.usermanagement.model.UserAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
//@Builder
@Entity
public class VerificationToken {
    private static final int TOKEN_EXPIRATION_TIME_IN_HOURS = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String token;
    private Instant expiryDate;
    private boolean revoked = false;
    private boolean expired = false;

    @OneToOne(targetEntity = UserAccount.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserAccount user;

    public VerificationToken() {
        setExpiryDate();
    }

    VerificationToken(final UserAccount user, final String token) {
        this.token = token;
        this.user = user;
        setExpiryDate();
    }

    private void setExpiryDate() {
        expiryDate = Instant.now().plusSeconds(TOKEN_EXPIRATION_TIME_IN_HOURS * 60 * 60);
    }

    boolean isValid() {
        return !(isExpired() || revoked);
    }

    private boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

    void setRevoked() {
        revoked = true;
    }

    @Override
    public String toString() {
        return "Token [String=" + token + "]" + "[Expires" + expiryDate + "]";
    }
}
