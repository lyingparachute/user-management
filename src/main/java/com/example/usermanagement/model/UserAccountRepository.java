package com.example.usermanagement.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    @Query(
      "select user from UserAccount user where user.userDetails.username = ?1"
    )
    Optional<UserAccount> findByUsername(String username);
}
